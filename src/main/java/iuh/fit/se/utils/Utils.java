package iuh.fit.se.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
	public static String formatPrice(Double price) {
		if (price == null)
			return "N/A";
		DecimalFormat formatter = new DecimalFormat("#,###");
		return formatter.format(price) + "₫";
	}
	 public static String formatDate(Object date, String pattern) {
		 if (date == null) return "";
	        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
	        
	        if (date instanceof Date) {
	            return sdf.format((Date) date);
	        } else if (date instanceof String) {
	            try {
	                // Parse từ định dạng ISO 8601 ("yyyy-MM-dd'T'HH:mm:ss")
	                SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	                Date parsedDate = isoFormat.parse((String) date);
	                return sdf.format(parsedDate);
	            } catch (ParseException e) {
	                return date.toString(); // Nếu lỗi, trả về nguyên bản String
	            }
	        }
	        return date.toString();
	    }
}
