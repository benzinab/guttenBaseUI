package de.akquinet.jbosscc.guttenBasePlugin.ui.ijTable;
// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

import com.intellij.icons.AllIcons;
import com.intellij.ide.DataManager;
import com.intellij.ide.IdeBundle;
import com.intellij.ide.highlighter.ArchiveFileType;
import com.intellij.ide.ui.SplitterProportionsDataImpl;
import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeRegistry;
import com.intellij.openapi.fileTypes.FileTypes;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SplitterProportionsData;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.*;
import com.intellij.ui.speedSearch.SpeedSearchSupply;
import com.intellij.ui.table.TableView;
import com.intellij.util.ArrayUtil;
import com.intellij.util.PlatformIcons;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.lang.module.Configuration;
import java.util.List;
import java.util.*;

/**
 * @author Gregory.Shrago
 */
public final class InjectionsSettingsUI extends SearchableConfigurable.Parent.Abstract implements Configurable.NoScroll {
    private final Project myProject;
    private final CfgInfo[] myInfos;

    private final JPanel myRoot;
    private final InjectionsTable myInjectionsTable;
    private final Map<String, String> mySupports = new LinkedHashMap<>();
    private final Map<String, AnAction> myEditActions = new LinkedHashMap<>();
    private final List<AnAction> myAddActions = new ArrayList<>();
    private final JLabel myCountLabel;


    public InjectionsSettingsUI(@NotNull Project project, CfgInfo[] myInfos) {
        myProject = project;
        this.myInfos = myInfos;

        final CfgInfo currentInfo = new CfgInfo(null, "Project");

        myRoot = new JPanel(new BorderLayout());

        myInjectionsTable = new InjectionsTable(getInjInfoList(null));

        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(myInjectionsTable);
        createActions(decorator);

        //myRoot.add(new TitledSeparator("Languages injection places"), BorderLayout.NORTH);
        myRoot.add(decorator.createPanel(), BorderLayout.CENTER);
        myCountLabel = new JLabel();
        myCountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        myCountLabel.setForeground(SimpleTextAttributes.GRAYED_ITALIC_ATTRIBUTES.getFgColor());
        myRoot.add(myCountLabel, BorderLayout.SOUTH);
    }

    private void createActions(ToolbarDecorator decorator) {
        myAddActions.sort((o1, o2) -> Comparing.compare(o1.getTemplatePresentation().getText(), o2.getTemplatePresentation().getText()));
        decorator.disableUpDownActions();
        decorator.setAddActionUpdater(e -> !myAddActions.isEmpty());
        decorator.setAddAction(this::performAdd);
        decorator.setRemoveActionUpdater(e -> {
            boolean enabled = false;
            for (InjInfo info : getSelectedInjections()) {
                if (!info.bundled) {
                    enabled = true;
                    break;
                }
            }
            return enabled;
        });
        decorator.setRemoveAction(button -> performRemove());

        decorator.setEditActionUpdater(e -> {
            AnAction edit = getEditAction();
            if (edit != null) edit.update(e);
            return edit != null && edit.getTemplatePresentation().isEnabled();
        });
        decorator.setEditAction(button -> performEditAction());

        //decorator.addExtraAction(
        //        new DumbAwareActionButton(IntelliLangBundle.messagePointer("action.AnActionButton.text.enable.selected.injections"),
        //                IntelliLangBundle.messagePointer("action.AnActionButton.description.enable.selected.injections"),
        //                PlatformIcons.SELECT_ALL_ICON) {
//
        //            @Override
        //            public void actionPerformed(@NotNull final AnActionEvent e) {
        //                performSelectedInjectionsEnabled(true);
        //            }
        //        });

        new DumbAwareAction(IdeBundle.messagePointer("action.Anonymous.text.toggle")) {
            @Override
            public void update(@NotNull AnActionEvent e) {
                SpeedSearchSupply supply = SpeedSearchSupply.getSupply(myInjectionsTable);
                e.getPresentation().setEnabled(supply == null || !supply.isPopupActive());
            }

            @Override
            public void actionPerformed(@NotNull final AnActionEvent e) {
                performToggleAction();
            }
        }.registerCustomShortcutSet(new CustomShortcutSet(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0)), myInjectionsTable);

        if (myInfos.length > 1) {
            AnActionButton shareAction =
                    new DumbAwareActionButton(("action.AnActionButton.text.move.to.ide.scope"),
                            PlatformIcons.IMPORT_ICON) {
                        {
                            addCustomUpdater(e -> {
                                CfgInfo cfg = getTargetCfgInfo(getSelectedInjections());
                                e.getPresentation().setText(cfg == getDefaultCfgInfo() ? ("label.text.move.to.ide.scope")
                                        : ("label.text.move.to.project.scope"));
                                return cfg != null;
                            });
                        }

                        @Override
                        public void actionPerformed(@NotNull final AnActionEvent e) {
                            final List<InjInfo> injections = getSelectedInjections();
                            final CfgInfo cfg = getTargetCfgInfo(injections);
                            if (cfg == null) return;
                            for (InjInfo info : injections) {
                                if (info.cfgInfo == cfg) continue;
                                if (info.bundled) continue;
                                info.cfgInfo.injectionInfos.remove(info);
                                cfg.addInjection(null);
                            }
                            final int[] selectedRows = myInjectionsTable.getSelectedRows();
                            myInjectionsTable.getListTableModel().setItems(getInjInfoList(myInfos));
                            TableUtil.selectRows(myInjectionsTable, selectedRows);
                        }

                        @Nullable
                        private CfgInfo getTargetCfgInfo(final List<? extends InjInfo> injections) {
                            CfgInfo cfg = null;
                            for (InjInfo info : injections) {
                                if (info.bundled) {
                                    continue;
                                }
                                if (cfg == null) cfg = info.cfgInfo;
                                else if (cfg != info.cfgInfo) return info.cfgInfo;
                            }
                            if (cfg == null) return null;
                            for (CfgInfo info : myInfos) {
                                if (info != cfg) return info;
                            }
                            throw new AssertionError();
                        }
                    };
            shareAction.setShortcut(new CustomShortcutSet(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, InputEvent.SHIFT_DOWN_MASK)));
            decorator.addExtraAction(shareAction);
        }
        decorator.addExtraAction(new DumbAwareActionButton(("action.AnActionButton.text.import"),
                ("action.AnActionButton.description.import"),
                AllIcons.Actions.Install) {

            @Override
            public void actionPerformed(@NotNull final AnActionEvent e) {
                doImportAction(e.getDataContext());
            }
        });
    }


    private void performEditAction() {
        final AnAction action = getEditAction();
        if (action != null) {
            final int row = myInjectionsTable.getSelectedRow();
            action.actionPerformed(new AnActionEvent(null, DataManager.getInstance().getDataContext(myInjectionsTable),
                    ActionPlaces.UNKNOWN, new Presentation(""), ActionManager.getInstance(), 0));
            myInjectionsTable.getListTableModel().fireTableDataChanged();
            myInjectionsTable.getSelectionModel().setSelectionInterval(row, row);
            //updateCountLabel();
        }
    }


    @Nullable
    private AnAction getEditAction() {
        final InjInfo info = getSelectedInjection();
        final String supportId = info == null? null : null;
        return supportId == null? null : myEditActions.get(supportId);
    }

    private void addInjection(final String injection) {
        final InjInfo info = getDefaultCfgInfo().addInjection(null);
        myInjectionsTable.getListTableModel().setItems(getInjInfoList(myInfos));
        final int index = myInjectionsTable.convertRowIndexToView(myInjectionsTable.getListTableModel().getItems().indexOf(info));
        myInjectionsTable.getSelectionModel().setSelectionInterval(index, index);
        TableUtil.scrollSelectionToVisible(myInjectionsTable);
    }

    private CfgInfo getDefaultCfgInfo() {
        return myInfos[0];
    }

    @Override
    public boolean hasOwnContent() {
        return true;
    }

    @Override
    protected Configurable[] buildConfigurables() {
        final ArrayList<Configurable> configurables = new ArrayList<>();
        configurables.sort((o1, o2) -> Comparing.compare(o1.getDisplayName(), o2.getDisplayName()));
        return configurables.toArray(new Configurable[0]);
    }

    @NotNull
    @Override
    public String getId() {
        return "IntelliLang.Configuration";
    }


    @Override
    public JComponent createComponent() {
        return myRoot;
    }

    @Override
    public void reset() {
        for (CfgInfo info : myInfos) {
            info.reset();
        }
        myInjectionsTable.getListTableModel().setItems(getInjInfoList(myInfos));
    }

    @Override
    public void apply() {
        for (CfgInfo info : myInfos) {
            info.apply();
        }
        reset();
    }

    @Override
    public boolean isModified() {
        return Arrays.stream(myInfos).anyMatch(CfgInfo::isModified);
    }

    private void performSelectedInjectionsEnabled(final boolean enabled) {
    }

    private void performToggleAction() {
        final List<InjInfo> selectedInjections = getSelectedInjections();
        boolean enabledExists = false;
        boolean disabledExists = false;
        for (InjInfo info : selectedInjections) {
            if (true) enabledExists = true;
            else disabledExists = true;
            if (enabledExists && disabledExists) break;
        }
        boolean allEnabled = !enabledExists && disabledExists;
        performSelectedInjectionsEnabled(allEnabled);
    }

    private void performRemove() {
        final int selectedRow = myInjectionsTable.getSelectedRow();
        if (selectedRow < 0) return;
        final List<InjInfo> selected = getSelectedInjections();
        for (InjInfo info : selected) {
            if (info.bundled) continue;
            info.cfgInfo.injectionInfos.remove(info);
        }
        myInjectionsTable.getListTableModel().setItems(getInjInfoList(myInfos));
        final int index = Math.min(myInjectionsTable.getListTableModel().getRowCount() - 1, selectedRow);
        myInjectionsTable.getSelectionModel().setSelectionInterval(index, index);
        TableUtil.scrollSelectionToVisible(myInjectionsTable);
    }

    private List<InjInfo> getSelectedInjections() {
        List<InjInfo> toRemove = new ArrayList<>();
        for (int row : myInjectionsTable.getSelectedRows()) {
            toRemove.add(myInjectionsTable.getItems().get(myInjectionsTable.convertRowIndexToModel(row)));
        }
        return toRemove;
    }

    @Nullable
    private InjInfo getSelectedInjection() {
        final int row = myInjectionsTable.getSelectedRow();
        return row < 0? null : myInjectionsTable.getItems().get(myInjectionsTable.convertRowIndexToModel(row));
    }

    private void performAdd(AnActionButton e) {
    }

    @Override
    @Nls
    public String getDisplayName() {
        return ("configurable.InjectionsSettingsUI.display.name");
    }

    @Override
    public String getHelpTopic() {
        return "reference.settings.injection.language.injection.settings";
    }

    private final class InjectionsTable extends TableView<InjInfo> {
        private InjectionsTable(final List<InjInfo> injections) {
            super(new ListTableModel<>(createInjectionColumnInfos(), injections, 1));
            setAutoResizeMode(AUTO_RESIZE_LAST_COLUMN);
            getColumnModel().getColumn(2).setCellRenderer(createLanguageCellRenderer());
            getColumnModel().getColumn(1).setCellRenderer(createDisplayNameCellRenderer());
            getColumnModel().getColumn(0).setResizable(false);
            setShowGrid(false);
            setShowVerticalLines(false);
            setGridColor(getForeground());
            TableUtil.setupCheckboxColumn(this, 0);

            new DoubleClickListener() {
                @Override
                protected boolean onDoubleClick(@NotNull MouseEvent e) {
                    final int row = rowAtPoint(e.getPoint());
                    if (row < 0) return false;
                    if (columnAtPoint(e.getPoint()) <= 0) return false;
                    myInjectionsTable.getSelectionModel().setSelectionInterval(row, row);
                    performEditAction();
                    return true;
                }
            }.installOn(this);

            final String[] maxName = new String[]{""};
            ContainerUtil.process(injections, injection -> {
                String languageId = null;
                Language language = null;
                String displayName = language == null ? languageId : language.getDisplayName();
                if (maxName[0].length() < displayName.length()) maxName[0] = displayName;
                return true;
            });
            Icon icon = FileTypes.PLAIN_TEXT.getIcon();
            int preferred = (int)(new JLabel(maxName[0], icon, SwingConstants.LEFT).getPreferredSize().width * 1.1);
            getColumnModel().getColumn(2).setMinWidth(preferred);
            getColumnModel().getColumn(2).setPreferredWidth(preferred);
            getColumnModel().getColumn(2).setMaxWidth(preferred);
            new TableViewSpeedSearch<InjInfo>(this) {
                @Override
                protected String getItemText(@NotNull InjInfo element) {
                    return "hha";
                }
            };
        }

    }

    private ColumnInfo[] createInjectionColumnInfos() {
        final TableCellRenderer booleanCellRenderer = createBooleanCellRenderer();
        final TableCellRenderer displayNameCellRenderer = createDisplayNameCellRenderer();
        final TableCellRenderer languageCellRenderer = createLanguageCellRenderer();
        final Comparator<InjInfo> languageComparator = null;
        final ColumnInfo[] columnInfos = {new ColumnInfo<InjInfo, Boolean>(" ") {
            @Override
            public Class getColumnClass() {
                return Boolean.class;
            }

            @Override
            public Boolean valueOf(final InjInfo o) {
                return true;
            }

            @Override
            public boolean isCellEditable(final InjInfo injection) {
                return true;
            }

            @Override
            public void setValue(final InjInfo injection, final Boolean value) {
            }

            @Override
            public TableCellRenderer getRenderer(final InjInfo injection) {
                return booleanCellRenderer;
            }
        }, new ColumnInfo<InjInfo, InjInfo>(("column.info.name")) {
            @Override
            public InjInfo valueOf(final InjInfo info) {
                return info;
            }

            @Override
            public Comparator<InjInfo> getComparator() {
                return null;
            }

            @Override
            public TableCellRenderer getRenderer(final InjInfo injection) {
                return displayNameCellRenderer;
            }
        }, new ColumnInfo<InjInfo, InjInfo>(("column.info.language")) {
            @Override
            public InjInfo valueOf(final InjInfo info) {
                return info;
            }

            @Override
            public Comparator<InjInfo> getComparator() {
                return languageComparator;
            }

            @Override
            public TableCellRenderer getRenderer(final InjInfo info) {
                return languageCellRenderer;
            }
        }};
        if (myInfos.length > 1) {
            final TableCellRenderer typeRenderer = createTypeRenderer();
            return ArrayUtil.append(columnInfos, new ColumnInfo<InjInfo, String>(("column.info.scope")) {
                @Override
                public String valueOf(final InjInfo info) {
                    return info.bundled ? "Built-in" : info.cfgInfo.title;
                }

                @Override
                public TableCellRenderer getRenderer(final InjInfo injInfo) {
                    return typeRenderer;
                }

                @Override
                public int getWidth(final JTable table) {
                    return table.getFontMetrics(table.getFont()).stringWidth(StringUtil.repeatSymbol('m', 6));
                }

                @Override
                public Comparator<InjInfo> getComparator() {
                    return (o1, o2) -> Comparing.compare(valueOf(o1), valueOf(o2));
                }
            });
        }
        return columnInfos;
    }

    private static BooleanTableCellRenderer createBooleanCellRenderer() {
        return new BooleanTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(final JTable table,
                                                           final Object value,
                                                           final boolean isSelected,
                                                           final boolean hasFocus,
                                                           final int row,
                                                           final int column) {
                return setLabelColors(super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column), table, isSelected, row);
            }
        };
    }

    private static TableCellRenderer createLanguageCellRenderer() {
        return new TableCellRenderer() {
            final JLabel myLabel = new JLabel();

            @Override
            public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus,
                                                           final int row,
                                                           final int column) {
                final InjInfo injection = (InjInfo)value;
                // fix for a marvellous Swing peculiarity: AccessibleJTable likes to pass null here
                if (injection == null) return myLabel;
                final String languageId = null;
                final Language language = null;
                final FileType fileType = language == null ? null : language.getAssociatedFileType();
                myLabel.setIcon(fileType == null ? null : fileType.getIcon());
                myLabel.setText(language == null ? languageId : language.getDisplayName());
                setLabelColors(myLabel, table, isSelected, row);
                return myLabel;
            }
        };
    }

    private TableCellRenderer createDisplayNameCellRenderer() {
        return new TableCellRenderer() {
            final SimpleColoredComponent myLabel = new SimpleColoredComponent();
            final SimpleColoredText myText = new SimpleColoredText();

            @Override
            public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus,
                                                           final int row,
                                                           final int column) {
                myLabel.clear();
                final InjInfo info = (InjInfo)value;
                // fix for a marvellous Swing peculiarity: AccessibleJTable likes to pass null here
                if (info == null) return myLabel;
                final SimpleTextAttributes grayAttrs = isSelected ? SimpleTextAttributes.REGULAR_ATTRIBUTES : SimpleTextAttributes.GRAYED_ATTRIBUTES;
                final String supportId = null;
                myText.append(supportId + ": ", grayAttrs);
                myText.appendToComponent(myLabel);
                myText.clear();
                setLabelColors(myLabel, table, isSelected, row);
                return myLabel;
            }
        };
    }

    private static TableCellRenderer createTypeRenderer() {
        return new TableCellRenderer() {
            final SimpleColoredComponent myLabel = new SimpleColoredComponent();

            @Override
            public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus,
                                                           final int row,
                                                           final int column) {
                myLabel.clear();
                final String info = (String)value;
                if (info == null) return myLabel;
                final SimpleTextAttributes grayAttrs = isSelected ? SimpleTextAttributes.REGULAR_ATTRIBUTES : SimpleTextAttributes.GRAYED_ATTRIBUTES;
                myLabel.append(info, grayAttrs);
                setLabelColors(myLabel, table, isSelected, row);
                return myLabel;
            }
        };
    }

    private static Component setLabelColors(final Component label, final JTable table, final boolean isSelected, final int row) {
        if (label instanceof JComponent) {
            ((JComponent)label).setOpaque(true);
        }
        label.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
        label.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        return label;
    }

    private void doImportAction(final DataContext dataContext) {
        final FileChooserDescriptor descriptor = new FileChooserDescriptor(true, false, false, false, true, false) {
            @Override
            public boolean isFileVisible(VirtualFile file, boolean showHiddenFiles) {
                return super.isFileVisible(file, showHiddenFiles) &&
                        (file.isDirectory() || "xml".equals(file.getExtension()) || FileTypeRegistry.getInstance().isFileOfType(file, ArchiveFileType.INSTANCE));
            }

            @Override
            public boolean isFileSelectable(VirtualFile file) {
                return FileTypeRegistry.getInstance().isFileOfType(file, StdFileTypes.XML);
            }
        };
        descriptor.setDescription(("dialog.file.chooser.description.please.select.the.configuration.file"));
        descriptor.setTitle(("dialog.file.chooser.title.import.configuration"));

        descriptor.putUserData(LangDataKeys.MODULE_CONTEXT, LangDataKeys.MODULE.getData(dataContext));

        final SplitterProportionsData splitterData = new SplitterProportionsDataImpl();
        splitterData.externalizeFromDimensionService("IntelliLang.ImportSettingsKey.SplitterProportions");

        final VirtualFile file = FileChooser.chooseFile(descriptor, myProject, null);
        if (file == null) return;
    }

    private static class CfgInfo {
        final Configuration cfg;
        final List<InjInfo> injectionInfos = new ArrayList<>();
        final String title;

        CfgInfo(Configuration cfg, final String title) {
            this.cfg = cfg;
            this.title = title;
            reset();
        }

        public void apply() {
        }

        public void reset() {
        }

        public InjInfo addInjection(final Object injection) {
            final InjInfo info = new InjInfo( this, false);
            injectionInfos.add(info);
            return info;
        }

        public boolean isModified() {
            return true;
        }

        public void replace(final List<? extends Object> originalInjections, final List<? extends Object> newInjections) {
        }

    }

    private static final class InjInfo {
        final CfgInfo cfgInfo;
        final boolean bundled;

        private InjInfo (CfgInfo cfgInfo, boolean bundled) {
            this.cfgInfo = cfgInfo;
            this.bundled = bundled;
        }
    }

    private static List<InjInfo> getInjInfoList(final CfgInfo[] infos) {
        return ContainerUtil.concat(infos, cfgInfo -> cfgInfo.injectionInfos);
    }

}

