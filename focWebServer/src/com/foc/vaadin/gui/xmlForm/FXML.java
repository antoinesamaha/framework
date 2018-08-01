package com.foc.vaadin.gui.xmlForm;

import com.foc.shared.xmlView.IXMLViewConst;

public interface FXML {
  public static final String DATA_ROOT                 		= "DATAROOT"         ;
  public static final String DATA_STORE                   = "DATASTORE["       ;
  
	public static final String DATA_SOURCE_DEFAULT       		= "DEFAULT"          ;

	public static final String TAG_REPORT                		= "Report"           ;
	public static final String TAG_FORM                  		= "Form"             ;
  public static final String TAG_FIELD                 		= "GuiField"         ;    
  public static final String TAG_CONDITION_FIELD          = "ConditionField"   ;
  public static final String TAG_TABLE                 		= "GuiTable"         ;
  public static final String TAG_GRID                   	= "Grid"             ;
  public static final String TAG_HTML_TABLE               = "HtmlTable"        ;
  public static final String TAG_TREE                  		= "GuiTree"          ;
  public static final String TAG_TREE_GRID                = "TreeGrid"         ;
  public static final String TAG_TABLE_GRID               = "TableGrid"        ;
  public static final String TAG_PIVOT                 		= "GuiPivot"         ;
  public static final String TAG_QR_CODE	                = "QRCode"           ;
  public static final String TAG_BAR_CODE	                = "BarCode"          ;
  public static final String TAG_GROUP_HEADER             = "GroupHeader"      ;
  public static final String TAG_GROUP_FOOTER             = "GroupFooter"      ;
  
  //Grid
  public static final String ATT_FROZEN_COLUMNS             = "frozenColumns"   ;
  public static final String ATT_GRID_SELECTION_MODE        = "gridSelectionMode";
  public static final String VAL_GRID_SELECTION_MODE_MULTI  = "Multi"           ;
  public static final String VAL_GRID_SELECTION_MODE_SINGLE = "Single"          ;
  
  public static final String TAG_DATA_MAP              		= "DataMap"          ;
  public static final String ATT_DATA_REPLACE          		= "replace"          ;
  public static final String ATT_DATA_WITH             		= "with"             ;
  
  public static final String TAG_PARAMETER                = "Parameter"        ;
  public static final String ATT_PARAM_NAME               =	"name"             ;
  public static final String ATT_PARAM_VALUE              = "value"            ;
  
  public static final String ATT_BREAKDOWN_NAME           = "breakdownName"    ;
  public static final String ATT_IMAGE_DIR                = "imageDir"         ;
  public static final String ATT_VISIBLE                  = "visible"          ;
  public static final String ATT_IS_HTML                  = "isHtml"           ;
  public static final String ATT_COLUMN_1                 = "column1"          ;
  public static final String ATT_COLUMN_2                 = "column2"          ;
  public static final String ATT_COLUMN_3                 = "column3"          ;
  public static final String ATT_HAS_BACKGROUND_COLOR     = "hasBackgroundStyle";

  public static final String ATT_HELP_INDEX               = "helpIndex"        ;
  public static final String ATT_HELP                     = "help"             ;
  public static final String TAG_SCREEN_HELP              = "ScreenHelp"       ;
  
  public static final String ATT_TAB_POSITION             = "position"         ;
  public static final String ATT_AVOID_ROW_BREAK          = "avoidRowBreak"    ;
  public static final String ATT_CAPTION_MODE             = "captionMode"      ;
  public static final String ATT_LINK                     = "link";
  public static final String ATT_RELOAD                   = "reload"           ;
  public static final String ATT_EXPAND                		= "expand"           ;
  public static final String ATT_STYLE                 		= "style"            ;
  public static final String ATT_MAX_CHARACTERS           = "maxCharacters"    ;
  public static final String ATT_SPLIT_POSITION        		= "splitPosition"    ;
  public static final String ATT_NAME                  		= "name"             ;
  public static final String ATT_VALUE                 		= "value"            ;
  public static final String ATT_DATA_PATH             		= "dataPath"         ;
  public static final String ATT_WIDTH                 		= "width"            ;
  public static final String ATT_LEFT                  		= "left"             ;
  public static final String ATT_RIGHT                 		= "right"            ;
  public static final String ATT_TOP                   		= "top"              ;
  public static final String ATT_HEIGHT                		= "height"           ;
  public static final String ATT_ROW                   		= "row"              ;
  public static final String ATT_COL                   		= "col"              ;
  public static final String ATT_ROWS                  		= "rows"             ;
  public static final String ATT_COLS                  		= "cols"             ;
  public static final String ATT_IDX                   		= "idx"              ;
  public static final String ATT_BORDER                		= "border"           ;
  public static final String ATT_SPACING               		= "spacing"          ;
  public static final String ATT_MARGIN                		= "margin"           ;
  public static final String ATT_ALIGNMENT             		= "alignment"        ;
  public static final String ATT_CAPTION               		= "caption"          ;
  public static final String ATT_ICON                   	= "icon"             ;
  public static final String ATT_GEAR_ENABLED             = "gearEnabled"      ;
  public static final String ATT_USE_POPUP_VIEW           = "usePopupView"     ;
  public static final String ATT_MORE_CAPTION          		= "moreCaption"      ;
  public static final String ATT_CAPTION_MARGIN        		= "captionMargin"    ;
  public static final String ATT_CAPTION_PROPERTY      		= "captionProperty"  ;//For TreeTable
  public static final String ATT_CAPTION_PROPERTY_2      	= "captionProperty2" ;//For TreeTable  
  public static final String ATT_SHOW_ICON                = "showIcon"         ;//true, false, iconOnly
  public static final String VAL_ICON_ONLY                = "iconOnly"         ;
  public static final String ATT_DESCRIPTION_CAPTION   		= "descriptionCaption";
  public static final String ATT_CAPTION_POSITION      		= "captionPos"       ;
  public static final String ATT_DESCRIPTION              = "description"      ;
  public static final String ATT_CAPTION_WIDTH         		= "captionWidth"     ;
  public static final String ATT_CAPTION_STYLE           	= "captionStyle"     ;
  public static final String ATT_FORM_TITLE            		= "formTitle"        ;
  public static final String ATT_TITLE            		    = "title"            ;
  public static final String ATT_WITH_APPLY            		= "withApply"        ;
  public static final String ATT_WITH_SAVE                = "withSave"         ;
  public static final String ATT_WITH_DISCARD          		= "withDiscard"      ;
  public static final String ATT_WITH_STATUS              = "withStatus"       ;  
  public static final String ATT_WITH_STAGE               = "withStage"        ;
  public static final String ATT_DISCARD_LINK          		= "discardLink"      ;
  public static final String ATT_APPLY_LINNK              = "applyLink"        ;
  public static final String ATT_VALUE_COMPUTE_LEVEL      = "computeLevel"     ;
  public static final String ATT_PIVOT_AGGREGATION_FORMULA = "aggregation"     ;
  public static final String ATT_WITH_PRINT            		= "withPrint"        ;
  public static final String ATT_WITH_PDF                 = "withPdf"					 ;
  public static final String ATT_WITH_MSWORD              = "withMSWord"			 ;
  public static final String ATT_ALLOW_REPORT_PRINT_AS_WORD = "reportPrintAsWord";
  public static final String ATT_ALLOW_REPORT_SEND_EMAIL    = "reportSendEMail"	 ;
  public static final String ATT_WITH_ATTACH           		= "withAttach"       ;
  public static final String ATT_WITH_EMAIL            		= "withEmail"        ;
  public static final String ATT_WITH_EMAIL_SEND_ICON     = "withSendIcon"     ;
  public static final String ATT_WITH_INTERNAL_EMAIL      = "withInternalEmail";
  public static final String ATT_WITH_TIPS            		= "withTips"         ;
  public static final String ATT_WITH_LOG                 = "withLog"          ;
  public static final String ATT_TREE_TABLE_PAGE_LENGTH		= "pageLength"       ;
  public static final String ATT_IN_LINE_EDITABLE      		= "inLineEditable"   ;
  public static final String ATT_ENABLE_DOWNLOAD       		= "enableDownload"   ;
  public static final String ATT_VISIBLE_WHEN          		= "visibleWhen"      ;
  public static final String ATT_FILTER_EXPRESSION        = "filterExpression" ;
  public static final String ATT_REMOVE_ZEROS             = "removeZeros"      ;
  public static final String ATT_SORTING_EXPRESSION       = "sortingExpression";
  public static final String ATT_EDITABLE                 = "editable"         ;
  public static final String ATT_SHOWDESCRIPTION          = "showDescription"  ;
  public static final String ATT_OPTION_GROUP             = "optionGroup"      ;
  public static final String ATT_COLLAPSING_ALLOWED       = "columnCollapse"   ;
  public static final String ATT_REDIRECT_ENABLED         = "redirectEnabled"  ;
  public static final String ATT_REDIRECT_CAPTION_PROPERTY= "redirectCaptionProperty";
  public static final String ATT_ADD_ENABLED              = "addEnabled"       ;
  public static final String ATT_TRANSACTION_FILTER_ENABLED   = "transactionFilterEnabled";
  public static final String ATT_TRANSACTION_COLORING_ENABLED = "transactionColoringEnabled";
  public static final String ATT_TRANSACTION_DEFAULT_COLORING = "transactionDefaultColoring";
  public static final String ATT_DUPLICATE_ENABLED        = "duplicateEnabled" ;
  public static final String ATT_STATUS_STYLE_ENABLED     = "statusSyleEnabled";
  public static final String ATT_OPEN_ENABLED             = "openEnabled"      ;
  public static final String ATT_OPEN_IN_NEW_TAB_ENABLED  = "openInNewTabEnabled";
  public static final String ATT_DOUBLE_CLICK_ENABLED     = "doubleClickEnabled" ;
  public static final String ATT_EXCEL_EXPORT_ENABLED     = "exportEnabled"    ;
  public static final String ATT_DELETE_ENABLED           = "deleteEnabled"    ;
  public static final String ATT_REFRESH_ENABLED          = "refreshEnabled"   ;
  public static final String ATT_IMMEDIATE                = "immediate"        ;
  public static final String ATT_WITH_FILTER              = "withFilter"       ;
  public static final String ATT_SHOW_VALIDATION_lAYOUT   = "showValidationLayout" ;
  public static final String ATT_FOOTER_FORMULA           = "footerFormula"     ;
  public static final String ATT_FORMULA                  = "formula"           ;
  public static final String ATT_BKDN_SHOW_TREE           = "showTree"          ;
  public static final String ATT_BKDN_DATE_START          = "startDate"         ;
  public static final String ATT_BKDN_DATE_END            = "endDate"           ;
  public static final String ATT_BKDN_CUT_OFF_DATE        = "cutOffDate"        ;
  public static final String ATT_BKDN_HIDE_WHEN_ALONE     = "hideWhenAlone"     ;
  public static final String ATT_DATE_GROUPING            = "dateGrouping"      ;
  public static final String ATT_TITLE_WHEN_EMPTY         = "titleWhenEmpty"    ;
  public static final String ATT_DESCRIP_WHEN_EMPTY       = "descriptionWhenEmpty";
  public static final String ATT_PIVOT_GRID               = "grid"              ;
  public static final String ATT_PIVOT_OPERATION          = "operation"         ;
  public static final String ATT_PIVOT_COLLAPSED          = "collapseTree"      ;
  public static final String ATT_PIVOT_REMOVE_EMPTY       = "removeEmptyNodes"  ;
  public static final String ATT_PIVOT_REMOVE_ONLY        = "removeOnlyChildren";
  public static final String ATT_BKDN_WRAPE_NATIVE        = "wrapNative"        ;
  public static final String ATT_LEAF_LEVEL               = "leafLevel"         ;
  public static final String ATT_GROUP_BY                 = "groupBy"           ;
  public static final String ATT_SORT_BY                  = "sortBy"            ;
  public static final String ATT_TITLE_CAPTION            = "titleCaption"      ;
  public static final String ATT_COMMIT_DATA              = "commitData"        ;
  public static final String ATT_EXPAND_RATIO             = "expandRatio"       ;
  public static final String ATT_INNER_GRID_LAYOUT_BORDER = "innerGridBorder"   ;
  public static final String ATT_SHOW_VIEW_SELECTOR       = "viewSelector"      ;
  public static final String ATT_MASK_DATA_FOUND_ERROR    = "maskDataFoundError";
  public static final String ATT_TREE_EXPANDED            = "expandTree"        ;
  public static final String ATT_REPLACE_CHECKBOX         = "replaceCheckbox"   ;
  public static final String ATT_TREE_COLOR_BY_LEVEL      = "colorByLevel"      ;
  public static final String ATT_LABEL_COLOR_BY_LEVEL     = "labelColorByLevel" ;
  public static final String ATT_PRINT_AND_EXIT           = "printAndExit"      ;
  public static final String ATT_HTML_TABLE_HIDE_OVERFLOW = "hideOverflow"      ;
  public static final String ATT_ADD_BUTTON_CAPTION       = "addButtonCaption"  ;
  public static final String ATT_DELETE_BUTTON_CAPTION    = "deleteButtonCaption";
  public static final String ATT_OPEN_BUTTON_CAPTION      = "openButtonCaption" ;
  public static final String ATT_SINGLE_SELECTION         = "singleSelection"   ;
  public static final String ATT_WITH_UPLOAD_BUTTON       = "withUploadButton"  ;
  public static final String ATT_ENCAPSULATE          		= "encapsulate"       ;
  public static final String ATT_PROMPT                   = "prompt"            ;
  public static final String ATT_TABINDEX                 = "tabindex"          ;
  public static final String ATT_AUTO_REFRESH             = "autoRefresh"       ;
  public static final String ATT_BUTTON_LINK_STYLE        = "linkStyle"         ;
  
  public static final String TAG_LABEL                     = "Label"             ;
  public static final String TAG_BUTTON                    = "Button"            ;
  public static final String TAG_NATIVE_BUTTON             = "NativeButton"      ;
  public static final String TAG_LINE                      = "Line"              ;
  public static final String TAG_TABLE_COLUMN              = "TableColumn"       ;
  public static final String TAG_PIVOT_BREAKDOWN           = "PivotBreakdown"    ;
  public static final String TAG_VERTICAL_PANEL            = "VerticalPanel"     ;
  public static final String TAG_ABSOLUTE_LAYOUT           = "AbsoluteLayout"    ;
  public static final String TAG_HORIZONTAL_LAYOUT         = "HorizontalLayout"  ;
  public static final String TAG_VERTICAL_LAYOUT           = "VerticalLayout"    ;
  public static final String TAG_HTML_LAYOUT               = "HTMLLayout"        ;
  public static final String TAG_CUSTOM_LAYOUT             = "CustomLayout"      ;
  public static final String TAG_PIVOT_LAYOUT              = "PivotLayout"       ;
  public static final String TAG_MORE_LAYOUT               = "MoreLayout"        ;
  public static final String TAG_GRID_LAYOUT               = "GridLayout"        ;
  public static final String TAG_TAB_LAYOUT                = "TabLayout"         ;
  public static final String TAG_PANEL                     = "Panel"             ;
  public static final String TAG_VALIDATION_SETTINGS       = "ValidationSettings";
  public static final String TAG_VERTICAL_SPLIT_LAYOUT     = "VerticalSplitLayout";
  public static final String TAG_HORIZONTAL_SPLIT_LAYOUT   = "HorizontalSplitLayout";
  public static final String TAG_BLOB_DISPLAY              = "Blob"              ;
  
  public static final String TAG_CHART                     = "GuiChart"     ;
  public static final String ATT_CHART_TITLE               = "chartTitle"   ;
  public static final String TAG_CHART_XAXIS               = "XAxis"        ;
  public static final String TAG_CHART_YAXIS               = "YAxis"        ;
  public static final String ATT_CHART_TYPE                = "type"         ;
  public static final String ATT_CHART_YAXIS_TITLE         = "yTitle"       ;
  public static final String ATT_CHART_XAXIS_TITLE         = "xTitle"       ;
  public static final String ATT_VALUE_PROPERTY            = "valueProperty";
  
  public static final String VAL_CHART_AREA                = "area"           ;
  public static final String VAL_CHART_LINE                = "line"           ;
  public static final String VAL_CHART_SPLINE              = "spline"         ;
  public static final String VAL_CHART_AREASPLINE          = "areaspline"     ;
  public static final String VAL_CHART_COLUMN              = "column"         ;
  public static final String VAL_CHART_BAR                 = "bar"            ;
  public static final String VAL_CHART_PIE                 = "pie"            ;
  public static final String VAL_CHART_SCATTER             = "scatter"        ;
  public static final String VAL_CHART_GAUGE               = "gauge"          ;
  public static final String VAL_CHART_AREARANGE           = "arearange"      ;
  public static final String VAL_CHART_COLUMNRANGE         = "columnrange"    ;
  public static final String VAL_CHART_AREASPLINERANGE     = "areasplinerange";
  
  //Tag for Form Fields
  public static final String TAG_INTEGER_FIELD             = "IntegerField"       ;
  public static final String TAG_EMAIL_FIELD               = "EmailField"         ;
  public static final String TAG_TEXT_FIELD                = "TextField"          ;
  public static final String TAG_DATE_FIELD                = "DateField"          ;
  public static final String TAG_TIME_FIELD                = "TimeField"          ;
  public static final String TAG_OBJECT_SELECTOR_FIELD     = "ObjectField"        ;
  public static final String TAG_MULTIPLE_CHOICE_FIELD     = "MultipleChoiceField";
  public static final String TAG_MULTIPLE_CHOICE_FIELD_FOC_DESC = "MultipleChoiceFocDescField";
  public static final String TAG_MULTIPLE_CHOICE_STRING_BASED_FIELD_FOC_DESC = "MultipleChoiceStringBasedField";
  public static final String TAG_XML_VIEW_SELECTOR         = "XMLViewSelector";
  public static final String TAG_CHECK_BOX_FIELD           = "CheckBoxField"      ;
  public static final String TAG_TEXT_AREA_FIELD           = "TextAreaField"      ;
  public static final String TAG_NUMERIC_FIELD             = "NumericField"       ;
  public static final String TAG_IMAGE_FIELD               = "ImageField"         ;
  public static final String TAG_PASSWORD_FIELD            = "PasswordField"      ;
  
  public static final String TAG_VIEW_KEY_OPEN             = "OpenForm"           ;
  public static final String TAG_VIEW_KEY_NEW              = "NewForm"            ;
  public static final String TAG_INCLUDE_XML               = "IncludeXML"         ;
  public static final String TAG_INCLUDE_XML_FOR_EACH      = "IncludeXMLForEach"  ;
  public static final String ATT_FOR_EACH_SEPARATOR        = "separator"          ;
  public static final String ATT_VIEW_KEY_STORAGE_NAME     = "storage"            ;
  public static final String ATT_VIEW_KEY_TYPE             = "type"               ;
  public static final String ATT_VIEW_KEY_FOR_NEW          = "forNew"             ;
  public static final String ATT_VIEW_KEY_CONTEXT          = "context"            ;
  public static final String ATT_VIEW_KEY_VIEW             = "view"               ;
  public static final String ATT_VIEW_KEY_DEFAULT_VIEW     = "defaultView"        ;
  public static final String ATT_VIEW_CONTAINER            = "containerWindow"    ;
  public static final String ATT_WRAP_TEXT                 = "wrapText"           ;
  public static final String ATT_AUTOCOMPLETE              = "autoComplete"       ;
  public static final String ATT_DIRECTION                 = "direction"          ;
  public static final String VAL_DIRECTION_HORIZONTAL      = "horizontal"         ;
  public static final String ATT_FILTERING_MODE            = "filteringMode"      ;
  public static final String VAL_FILTERING_MODE_STARTS_WITH= "startsWith"         ;
  public static final String VAL_FILTERING_MODE_CONTAINS   = "contains"           ;
  
  public static final String ATT_TEXT_AREA_TOOLBAR         = "toolbar"            ;
  public static final String ATT_TEXT_AREA                 = "textArea"           ;
  public static final String ATT_FORMAT                    = "format"             ;
  public static final String ATT_REQUIRED                  = "required"           ;
  
  //Image Field
  public static final String ATT_SHOW_IMAGE                = "showImage"          ;
  
  public static final String VAL_VIEW_KEY_TYPE__FORM       = IXMLViewConst.TYPE_NAME_FORM;
  public static final String VAL_VIEW_KEY_TYPE__TABLE      = IXMLViewConst.TYPE_NAME_TABLE;
  public static final String VAL_VIEW_KEY_TYPE__TREE       = IXMLViewConst.TYPE_NAME_TREE;
  public static final String VAL_VIEW_KEY_TYPE__PIVOT      = IXMLViewConst.TYPE_NAME_PIVOT;
  
  public static final String VAL_VIEW_CONTAINER__NONE         = "none"        ;
  public static final String VAL_VIEW_CONTAINER__SAME_WINDOW  = "same window" ;
  public static final String VAL_VIEW_CONTAINER__POPUP_WINDOW = "popup window";
  public static final String VAL_VIEW_CONTAINER__INNER_LAYOUT = "inner layout";
  public static final String VAL_VIEW_CONTAINER__NEW_TAB      = "new tab" ;
  
  public static final String VAL_INLINE_EDITABLE           = "optional";
  
  public static final String VAL_CAPTION_POS__TOP   = "top"  ;
  public static final String VAL_CAPTION_POS__LEFT  = "left" ;
  public static final String VAL_CAPTION_POS__RIGHT = "right";
  
  public static final String COL_TREE_NODE_TITLE       = "NODE_TITLE" ;
  
  public static final String TAG_SHOW_FORM       = "ShowForm" ;
  
  public static final String VAL_TREE_TABLE_FIT_SIZE = "fit";
  
  //HTML File
  public static final String ATT_HTML_FILE_NAME        = "html";
  public static final String ATT_FHTML_FILE_NAME       = "fhtml";
  
  public static final String MS_STYLE_VAL_BOLD         = "bold";
  public static final String MS_STYLE_VAL_ITALIC       = "italic";
  public static final String MS_STYLE_VAL_OBLIQUE      = "oblique";
  public static final String MS_STYLE_VAL_UNDERLINE    = "underline";
  public static final String MS_STYLE_VAL_FONT_FAMILY  = "wff-";
  public static final String MS_STYLE_VAL_FONT_SIZE    = "f";
  public static final String MS_STYLE_VAL_ALIGN_LEFT   = "text-left";
  public static final String MS_STYLE_VAL_ALIGN_RIGHT  = "text-right";
  public static final String MS_STYLE_VAL_ALIGN_CENTER = "text-center";
  
  public static final String ATT_IMPORT_COMMIT_WITH_PARENT = "commitWithParent";
  
}
