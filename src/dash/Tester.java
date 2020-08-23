package dash;

import java.io.IOException;  
import java.io.File;

import org.jsoup.Jsoup;  
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;  
import org.jsoup.select.Elements; 

public class Tester {

	public static final String FOLDER_PATH = "C:\\Users\\Batgerel\\Desktop" ;
	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36";
	public static void main(String[] args) throws IOException {
		
		Document page = Jsoup.connect("http://reddit.com/r/politics").userAgent(USER_AGENT)
				.ignoreHttpErrors(true)
				.ignoreContentType(true)
				.referrer("http://www.google.com")
				.timeout(0)
				.followRedirects(true)
				.get();

		System.out.println(page.title());
		String imgName;
		String path;
		String format;
		int i=0;
	    
		Elements links = page.select("a[href]");
        Elements media = page.select("[src]");
        Elements imports = page.select("link[href]");

        print("\nMedia: (%d)", media.size());
        for (Element src : media) {
            if (src.normalName().equals("img")){
                print(" * %s: <%s> %sx%s (%s)",
                        src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
                        trim(src.attr("alt"), 20));
                imgName = src.attr("abs:src");
                format = imgCheck(imgName);
             
                path = "C:\\Users\\Batgerel\\Desktop\\Images\\"+String.valueOf(i)+format;
                i++; // image name changing
                File out = new File(path);
                new Thread(new Download(imgName,out)).start(); // Starts Downloading File
            }
            else
                print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
        }

        print("\nImports: (%d)", imports.size());
        for (Element link : imports) {
            print(" * %s <%s> (%s)", link.tagName(),link.attr("abs:href"), link.attr("rel"));
        }

        print("\nLinks: (%d)", links.size());
        for (Element link : links) {
            print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
        }
        
	}
	
    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }
	
    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }
    
    
    private static String imgCheck(String str){
    	String png = ".png";
    	String jpg = ".jpg";
    	String jpeg = "jpeg"; 
    	String gif = ".gif";
    	String last = "";
    	last = str.substring(str.length()-4);
		if(last.equals(png)) {
			return png;
		} else if(last.equals(jpg) || last.equals(".JPG")) {
			return jpg;
		} else if(last.equals(jpeg) || last.equals(".JPEG")){
			return jpeg;
		} else if(last.equals(gif)) {
			return gif;
		} else return ".JPEG";
  }
    


}
