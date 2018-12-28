package com.tysoft.common;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * POI导出Excel
 * @author ldr
 * @date 2018-07-31
 */
public class POIHSSFUtils {
	
	/** 单元格类型 **/
	public static enum Type {
		TITLE, COLUMN, DATA
	}
	
	private HSSFCellStyle titleStyle;
	private HSSFCellStyle columnStyle;
	private HSSFCellStyle defaultStyle;
	private HSSFWorkbook hssfWorkbook;
	private HSSFFont defaultFont;
	private HSSFFont columnFont;
	private HSSFFont titleFont;
	private String fileName;
	private HSSFSheet hssfSheet;
	private int columnWidth;
	private int rowHeight;
	private int titleHeight;
	
	
	public int getRowHeight() {
		return rowHeight;
	}

	public void setRowHeight(int rowHeight) {
		this.rowHeight = rowHeight;
	}
	
	public int getTtitleHeight() {
		return titleHeight;
	}

	public void setTtitleHeight(int titleHeight) {
		this.titleHeight = titleHeight;
	}

	public HSSFFont getDefaultFont() {
		return defaultFont;
	}

	public void setDefaultFont(HSSFFont defaultFont) {
		this.defaultFont = defaultFont;
	}

	public HSSFFont getColumnFont() {
		return columnFont;
	}

	public void setColumnFont(HSSFFont columnFont) {
		this.columnFont = columnFont;
	}

	public HSSFFont getTitleFont() {
		return titleFont;
	}

	public void setTitleFont(HSSFFont titleFont) {
		this.titleFont = titleFont;
	}

	public HSSFCellStyle getTitleStyle() {
		return titleStyle;
	}

	public void setTitleStyle(HSSFCellStyle titleStyle) {
		this.titleStyle = titleStyle;
	}

	public HSSFCellStyle getColumnStyle() {
		return columnStyle;
	}

	public void setColumnStyle(HSSFCellStyle columnStyle) {
		this.columnStyle = columnStyle;
	}

	public HSSFCellStyle getDefaultStyle() {
 		return defaultStyle;
	}
	
	public void setDefaultStyle(HSSFCellStyle defaultStyle){
		this.defaultStyle = defaultStyle;
	}

	public int getColumnWidth() {
		return columnWidth;
	}

	public void setColumnWidth(int columnWidth) {
		this.columnWidth = columnWidth;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public HSSFWorkbook getHssfWorkbook() {
		return hssfWorkbook;
	}
	
	public void setHssfWorkbook(HSSFWorkbook hssfWorkbook) {
		this.hssfWorkbook = hssfWorkbook;
	}

	public HSSFSheet getHssfSheet() {
		return hssfSheet;
	}

	public void setHssfSheet(HSSFSheet hssfSheet) {
		this.hssfSheet = hssfSheet;
	}
	
	private HSSFCellStyle initStyle(HSSFCellStyle style){
		style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
//		style.setWrapText(true);
		return style;
	}
	
	/**
	 *	@param fileName
	 *	@param titleHeight
	 *	@param rowHeight
	 *	@param isBold
	 *	@param fontName
	 */
	public POIHSSFUtils(String fileName,int titleHeight, int rowHeight,Boolean isBold,String fontName) {
		this.fileName = fileName;
		this.hssfWorkbook = new HSSFWorkbook();
		this.hssfSheet = hssfWorkbook.createSheet(fileName);
		this.defaultStyle = hssfWorkbook.createCellStyle();
		this.titleStyle = hssfWorkbook.createCellStyle();
		this.columnStyle = hssfWorkbook.createCellStyle();
		defaultFont = this.hssfWorkbook.createFont();
		columnFont = this.hssfWorkbook.createFont();
		titleFont = this.hssfWorkbook.createFont();
		
		defaultFont.setFontName(fontName);
		defaultFont.setBold(isBold);
		defaultFont.setFontHeightInPoints((short) 10);
		defaultStyle.setFont(defaultFont);
		defaultStyle = initStyle(defaultStyle);
		
		columnFont.setFontName(fontName);
		columnFont.setBold(isBold);
		columnFont.setFontHeightInPoints((short) 10);
		columnStyle.setFont(columnFont);
		columnStyle = initStyle(columnStyle);
		
		titleFont.setFontName("黑体");
		titleFont.setBold(true);
		titleFont.setFontHeightInPoints((short) 18);
		titleStyle.setFont(titleFont);
		titleStyle = initStyle(titleStyle);
		
		columnWidth = 12*512;
		this.rowHeight = rowHeight == 0 ? 318 : rowHeight;
		this.titleHeight = titleHeight == 0 ? 500 : titleHeight;
	}
	
	/**
	 * 创建标题
	 * @param title
	 * @param startRow
	 * @param startCol
	 * @param rowHeight
	 */
	public void createTitle(String title,int startRow,int startCol,int rowHeight) {
		HSSFRow row;
		HSSFCell cell;
		if(StringUtils.isNotBlank(title)){
			row = hssfSheet.createRow(startRow);
			row.setHeight((short)rowHeight);
			cell = row.createCell(startCol);
			HSSFRichTextString richString  = new HSSFRichTextString(title);
			richString.applyFont(titleFont);
			cell.setCellValue(richString);
		}
	}
	
	/**
	 * 创建标题、表头
	 * @param title
	 * @param startRow
	 * @param theads
	 * @param theads2
	 * @param merges
	 */
	public void createTitleAndHeader(String title,int startRow,String[] theads,String[] theads2,String[] merges){ 
		HSSFRow row;
		HSSFCell cell; 
		if(StringUtils.isNotBlank(title)){
			row = hssfSheet.createRow(startRow);
			row.setHeight((short)900);
			cell = row.createCell(startRow);  
			cell.setCellValue(title);
			cell.setCellStyle(titleStyle);
			this.hssfSheet.addMergedRegion(new CellRangeAddress(startRow,startRow,0,theads.length-1));
		}
		if(theads != null && theads.length > 0){
			row = this.hssfSheet.createRow(startRow+1);
			fillTitleCell(row, theads, null);
		}
		if(theads2 != null && theads2.length > 0){
			row = this.hssfSheet.createRow(startRow+2); 
			fillTitleCell(row , theads2, null);
		}
		if(merges != null && merges.length > 0 )this.mergeTitle(merges);
		
    }
	
	/**
	 * 填充表头数据
	 * @param startRow
	 * @param columnWidth
	 * @param theads
	 */
	public void fillTitleCell(int startRow,String[] theads,List<HSSFCellStyle> styleList){
		HSSFRow row = this.hssfSheet.createRow(startRow);
		if(theads != null && theads.length > 0){
			row = this.hssfSheet.createRow(startRow); 
			fillTitleCell(row , theads ,styleList);
		}
	}

	/**
	 * 填充表头数据
	 * @param row
	 * @param columnWidth
	 * @param theads
	 */
	public void fillTitleCell(HSSFRow row,String[] theads,List<HSSFCellStyle> styleList){
		for(int i = 0 ;  i < theads.length ; i++){
			String value = theads[i];
			row.setHeight((short)titleHeight);
			HSSFCell cell = row.createCell(i);
			this.hssfSheet.setColumnWidth(i, columnWidth); 
			if(value.contains(",")){
				this.hssfSheet.setColumnWidth(i,Integer.parseInt(value.split(",")[1]));
				value = value.split(",")[0];
			}
			HSSFCellStyle style = styleList==null || styleList.size()<(i+1)|| styleList.get(i)==null ? columnStyle : styleList.get(i);
			cell.setCellStyle(style);  
			cell.setCellValue(value);  
		}
	}
	
	
	/**
	 * 合并表头
	 * @param merges
	 */
	public void mergeTitle(String[] merges){
		for(int i = 0 ; i < merges.length; i++){
			String info = merges[i];
			if(info.contains(",")){
				String [] mergeNums = info.split("\\,");
				hssfSheet.addMergedRegion(new CellRangeAddress(Integer.parseInt(mergeNums[0]),Integer.parseInt(mergeNums[1]),Integer.parseInt(mergeNums[2]),Integer.parseInt(mergeNums[3])));
			}
		}
	}
	
	/**
	 * 填充单元格数据
	 * @param list
	 * @param startRow
	 * @param conditions
	 */
	public void fillCell(List<String[]> list,List<HSSFCellStyle> styleList,int startRow,String[] conditions){
		HSSFCell cell = null;
		List<String> mergeList = new ArrayList<String>();
		if(list != null && list.size() > 0){
			for(int i = 0 ; i < list.size() ; i++){
				HSSFRow row = hssfSheet.createRow(startRow);
				String[] arr = list.get(i);
				for(int j = 0 ; j < arr.length;j++){
					row.setHeight((short)rowHeight);
					cell = row.createCell(j);
					cell.setCellValue(arr[j]);
					cell.setCellStyle(styleList == null || styleList.size() < (j+1) || styleList.get(j) == null ? defaultStyle : styleList.get(j));
					
					for(int k = 0 ; k < conditions.length ; k++){
						String condition = conditions[k].split("\\,")[0];
						int row1 = Integer.parseInt(conditions[k].split("\\,")[1]);
						int row2 = Integer.parseInt(conditions[k].split("\\,")[2]);
						int col1 = Integer.parseInt(conditions[k].split("\\,")[3]);
						int col2 = Integer.parseInt(conditions[k].split("\\,")[4]);
						if(condition.equals(arr[j])){
							int r = cell.getRow().getRowNum();
							int c = cell.getColumnIndex();
							String merge = (r+row1)+","+(r+row2)+","+(c+col1)+","+(c+col2);
							mergeList.add(merge);
							if(StringUtils.isNotBlank(condition)){
								HSSFCell newCell = row.getCell(c+col1);
								if(null != newCell){
									newCell.setCellValue(condition);
								}
							}
						}
					}
				}
				startRow++;
			}
			//合并单元格
			for(String mergeStr : mergeList){
				String[] mergeArr = mergeStr.split("\\,");
				Integer sRow = Integer.parseInt(mergeArr[0]);
				Integer eRow = Integer.parseInt(mergeArr[1]);
				Integer sCol = Integer.parseInt(mergeArr[2]);
				Integer eCol = Integer.parseInt(mergeArr[3]);
				hssfSheet.addMergedRegion(new CellRangeAddress(sRow,eRow,sCol,eCol));
			}
		}else{
			//如果没有数据，显示“没有找到匹配的记录”
			int rowNum = hssfSheet.getLastRowNum();
			HSSFRow row = hssfSheet.getRow(rowNum);
			int columnNum = row.getLastCellNum();
			row = hssfSheet.createRow(startRow);
			row.setHeight((short)rowHeight);
			for(int i = 0 ; i < columnNum ; i++){
				cell = row.createCell(i);
				cell.setCellStyle(defaultStyle);
			}
			hssfSheet.addMergedRegion(new CellRangeAddress(startRow,startRow,0,columnNum-1));
			row.getCell(0).setCellStyle(defaultStyle);
			row.getCell(0).setCellValue("没有找到匹配的记录");
		}
		
	}
	/**
	 * 获取单元格数据
	 * @param returnStr
	 * @param returnInfo
	 * @param resultColumns
	 * @return
	 * @throws Exception
	 */
	public List<String[]> getData(String returnStr, String returnInfo,String[] resultColumns) throws Exception{
		JSONObject jsonObject = JSONObject.fromObject(returnStr);
		String str=jsonObject.getString(returnInfo);
		List<String[]> rows = new ArrayList<String[]>();
		if(StringUtils.isNotBlank(str)){
			JSONArray jsonArr = JSONArray.fromObject(str);
			for(int j=0;j<jsonArr.size();j++){
				String [] arr = new String[resultColumns.length];
				for(int k = 0 ; k < resultColumns.length; k++){
					if(resultColumns[k].equals("@sequence")){
						arr[k] = String.valueOf(j+1);
						continue;
					}
					arr[k] = jsonArr.getJSONObject(j).getString(resultColumns[k]); 
				}
	            rows.add(arr);
			}
		}
		return rows;
	}
	/**
	 * 合并单元格
	 * @param startRow
	 * @param endRow
	 * @param startColumn
	 * @param endColumn
	 */
	public void mergeCell(int startRow,int endRow,int startColumn,int endColumn){
		hssfSheet.addMergedRegion(new CellRangeAddress(startRow,endRow,startColumn,endColumn));
	}
	/**
	 * 给单独某个单元格赋值
	 * @param row
	 * @param column
	 * @param value
	 */
	public void setCellValue(int rowIndex,int columnIndex,String value){
		HSSFRow row = getHssfSheet().getRow(rowIndex);
		HSSFCell cell = null;
		if(row!= null) 
			cell = row.getCell(columnIndex);
		if(cell != null)
			cell.setCellValue(value);
	}
	public void fillBlank(List<String[]> rows,int[] columns,String replaceWord){
		if(columns != null && columns.length > 0){
			for(String[] arr : rows){
				for(int j = 0 ; j < columns.length ; j++){
					int columnNum = columns[j];
					if(columnNum > 0){
						String value = arr[columnNum];
						if(!StringUtils.isNotBlank(value)){
							arr[columnNum] = replaceWord;
						}else if(replaceWord.contains("%")){
							arr[columnNum] += "%";
						}
					}
				}
			}
		}
	}
	/**
	 * 输出文件
	 * @param response
	 */
	public void export(HttpServletResponse response){
		try{
		    //设置编码
			response.setCharacterEncoding("UTF-8");
			//设置文件格式
			response.setHeader("content-Type", "application/vnd.ms-excel");
			//设置文件名称
		    response.setHeader( "Content-Disposition", "attachment;filename=\""+ new String( this.fileName.getBytes( "gb2312" ), "ISO8859-1" )+ ".xls" + "\"" );
		    hssfWorkbook.write(response.getOutputStream());
		} catch (Exception e) {  
			e.printStackTrace();
		}  
	}

}
