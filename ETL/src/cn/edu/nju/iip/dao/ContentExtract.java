package cn.edu.nju.iip.dao;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ContentExtract {
public static String PageAnal(String html){
Document doc = Jsoup.parse(html);

Elements es = doc.getAllElements();
int temp_length = 0;
Element longest = es.first();
for (int i = 0; i < es.size(); i++){
Element temp = es.get(i);
temp.removeAttr("href");
temp.removeAttr("src");
if (temp.tagName().equals("img")){
temp.remove();
continue;
}
if (temp.tagName().equals("p") && temp.text().length() > temp_length){
temp_length = temp.text().length();
longest = temp;
}
else if (!temp.tagName().equals("div") && !temp.ownText().contains("='") && !temp.ownText().contains("=\"") && temp.ownText().length() > temp_length){
temp_length = temp.ownText().length();
longest = temp;
}
}

String content_tag_name = longest.tagName();
String all = "";
if (longest.parent() == null){
all = longest.text();
all += "|&|" + longest.toString();
}
else{
String temp = HTMLText(longest.parent().parent(), content_tag_name);
all = Text(longest.parent().parent(), content_tag_name);
all += "|&|" + temp;
}

return all;
}

public static String HTMLText(Element e, String content_tag_name){
String result = "";
for (Element ee : e.children()){
int flag = 0;
if (ee.tagName().equals("p")){
result += ee;
flag = 1;
}
else if (ee.tagName().equals(content_tag_name)){
result += ee;
flag = 1;
}
else {
if (ee.children() == null){
//	 System.out.println("叶节点"+ee.tagName());
}
else{
String temp = HTMLText(ee, content_tag_name);
if (!temp.equals("")){
result += temp;
flag = 1;
}
}
}
if (flag == 0){
ee.remove();
}
}
return result;

}

public static String Text(Element e, String content_tag_name){
String result = "";
for (Element ee :e.children()){
if (ee.tagName().equals("p"))
result += ee.text();
else if (ee.tagName().equals(content_tag_name))
result += ee.ownText();
else
result += Text(ee, content_tag_name);
}
return result;
}

public static String getContent(String url) {
String content = "";
try{
String html = Jsoup.connect(url).get().html();
String[] split = PageAnal(html).split("\\|\\&\\|");
content = split[0];
}catch(Exception e) {
return "";
}
return content;
}

public static void main(String[] args) throws IOException {
String url = "http://www.cqbnjw.gov.cn/html/514592436738.html";
System.out.println(getContent(url));
}
}