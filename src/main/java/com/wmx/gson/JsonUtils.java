package com.wmx.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;

/**
 * @author wangMaoXiong
 * @version 1.0
 * @date 2020/7/7 20:14
 */
public class JsonUtils {
    /**
     * Json 格式字符串 -> 解析为 JSON 对象
     */
    @Test
    public void parserJsonStrToJsonObj() {
        String jsonStr = "{\"pId\":9527,\"pName\":\"华安\",\"birthday\":\"Nov 23, 2018 1:50:56 PM\",\"isMarry\":true}";

        JsonParser jsonParser = new JsonParser();
        /**JsonElement parse(String json)
         * 如果被解析的字符串不符合 json 格式，则抛出异常*/
        JsonObject jsonObject = jsonParser.parse(jsonStr).getAsJsonObject();

        /**JsonElement get(String memberName)
         * 注意：如果 get 的 key 不存在，则返回 null，如果不加判断而进行取值的话，会抛：java.lang.NullPointerException
         * */
        Integer pId = jsonObject.get("pId").getAsInt();
        String pName = jsonObject.get("pName").getAsString();

        System.out.println(jsonObject);
        //输出：{"pId":9527,"pName":"华安","birthday":"Nov 23, 2018 1:50:56 PM","isMarry":true}
        System.out.println(pId + "\t" + pName);
        //输出：9527	华安
    }
    /**
     * Json 数组字符串 -> 解析为 JSON 数组对象
     */
    public static void parserJsonStrToJsonArray() {
        String jsonArrayStr = "[{\"pId\":9527,\"pName\":\"华安\",\"isMarry\":true},{\"pId\":8866,\"pName\":\"宁王\",\"isMarry\":false}]";
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = jsonParser.parse(jsonArrayStr).getAsJsonArray();

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            System.out.println((i + 1) + ":" + jsonObject + "\t\t" + jsonObject.get("pName").getAsString());
        }
        //输出：1:{"pId":9527,"pName":"华安","isMarry":true}		华安
        //输出：2:{"pId":8866,"pName":"宁王","isMarry":false}		宁王
    }
    /**
     * 手动 创建 JSON对象 并添加基本类型属性
     */
    public static void createJsonObj() {
        /**
         * JsonObject 添加基本类型属性使用 addProperty(String property, Number value)、 addProperty(String property, String value) 等
         * JsonObject 添加复杂类型属性使用 add(String property, JsonElement value)
         * JsonObject 与 JsonArray 都是 JsonElement 类的子类
         * 提示：如果添加的 key 已经存在，则后面的会覆盖前面的
         */
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("pId", 9527);
        jsonObject.addProperty("pName", "华安");
        jsonObject.addProperty("isMarry", true);

        /**
         * JsonObject 取值使用 JsonElement get(String memberName)，根据 JsonElement 可以获取任意类型
         * JsonObject 可以直接获取 JsonArray：JsonArray getAsJsonArray(String memberName)
         * JsonObject 可以直接获取 JsonObject：JsonObject getAsJsonObject(String memberName)
         */
        System.out.println(jsonObject + "\t" + jsonObject.get("pName").getAsString());
        //输出：{"pId":9527,"pName":"华安","isMarry":true}	华安
    }
    /**
     * 创建 JSON 数组
     */
    public static void createJsonArray() {
        String json1 = "{\"pId\":9527,\"pName\":\"华安\",\"isMarry\":true}";
        String json2 = "{\"pId\":1200,\"pName\":\"安禄山\",\"isMarry\":false}";

        JsonObject jsonObject1 = new JsonParser().parse(json1).getAsJsonObject();
        JsonObject jsonObject2 = new JsonParser().parse(json2).getAsJsonObject();

        JsonArray jsonArray = new JsonArray();
        jsonArray.add(jsonObject1);
        jsonArray.add(jsonObject2);

        System.out.println(jsonArray);
        //输出：[{"pId":9527,"pName":"华安","isMarry":true},{"pId":1200,"pName":"安禄山","isMarry":false}]
    }
    /**
     * 删除 JsonObject 的某个属性
     */
    public static void delJsonObjproperty() {
        String json = "{\"pId\":9527,\"pName\":\"华安\",\"isMarry\":true}";
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        System.out.println("删除前：" + jsonObject);
        //输出：删除前：{"pId":9527,"pName":"华安","isMarry":true}
        /**
         * JsonElement remove(String property)
         * 用于删除 JsonObject 的属性，返回被删除的属性的值，原 JsonObject 会改变
         * 与get取值同理，如果 remove 的属性值不存在，则返回 null
         */
        String delProperty = jsonObject.remove("pName").getAsString();

        System.out.println("删除 " + delProperty + " 后：" + jsonObject);
        //输出：删除 华安 后：{"pId":9527,"isMarry":true}
    }
    /**
     * 修改 JsonObject 属性，与添加一样使用 addProperty，当 key 已经存在时，会覆盖旧值
     */
    public static void updateJsonObjproperty() {
        String json = "{\"currentOnlineNumber\":\"101\",\"name\":\"汪茂雄\"}";
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        System.out.println("修改前：" + jsonObject);

        jsonObject.addProperty("name", "赵丽颖");
        System.out.println("修改后：" + jsonObject);
    }
    /**
     * 获取 JsonObject 属性值
     */
    public static void getJsonObjproperty() {
        String json = "{\"pId\":9527,\"pName\":\"华安\",\"Dog\":{\"dName\":\"小黄\",\"color\":\"yellow\"}}";
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        System.out.println("操作对象：" + jsonObject);

        /**
         * JsonElement get(String memberName)：如果 memberName 不存在，则返回 null
         * 所以除非应用中明确知道key存在，否则应该加以判断
         */
        Integer pId = jsonObject.get("pId").getAsInt();
        System.out.println("pId=" + pId);

        /** boolean has(String memberName)
         * 判断 JsonObject 中是否存在某个 key*/
        if (!jsonObject.has("name")) {
            System.out.println("name 属性不存在...");
        }

        /**
         * JsonObject 取值使用 JsonElement get(String memberName)，根据 JsonElement 可以获取任意类型
         * JsonObject 可以直接获取 JsonArray：JsonArray getAsJsonArray(String memberName)
         * JsonObject 可以直接获取 JsonObject：JsonObject getAsJsonObject(String memberName)
         */
        JsonObject dogJson = jsonObject.getAsJsonObject("Dog");
        System.out.println(dogJson);
    }
    /**
     * 实际应用中，如果业务比较复杂，则通常都会嵌套 json，如下所示：
     * [{"pId":110,"pName":"华安","Dog":{"dName":"小黄","color":"yellow"}},{"pId":120,"pName":"安禄山","Dog":{"dName":"阿毛","color":"red"}}]
     */
    public static void complexParsen() {
        /**两只小狗 json 字符串*/
        String dog1JsonStr = "{\"dName\":\"小黄\",\"color\":\"yellow\"}";
        String dog2JsonStr = "{\"dName\":\"阿毛\",\"color\":\"red\"}";

        /**将 json 字符串解析为 JsonObject 对象*/
        JsonObject dog1Json = new JsonParser().parse(dog1JsonStr).getAsJsonObject();
        JsonObject dog2Json = new JsonParser().parse(dog2JsonStr).getAsJsonObject();

        /**创建两个用户的 JsonObject 对象*/
        JsonObject user1Json = new JsonObject();
        JsonObject user2Json = new JsonObject();

        /**添加普通值*/
        user1Json.addProperty("pId", 110);
        user1Json.addProperty("pName", "华安");
        /**添加JsonObject对象
         * 注意：添加的对象，而不应该是符合 json 格式的字符串*/
        user1Json.add("Dog", dog1Json);

        user2Json.addProperty("pId", 120);
        user2Json.addProperty("pName", "阿毛");
        user2Json.add("Dog", dog2Json);

        /**创建 JsonArray 用于存放 JsonObject
         * 同样添加的应该是对象，而不应该是符合格式的字符串*/
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(user1Json);
        jsonArray.add(user2Json);

        System.out.println(jsonArray);
        //输出：[{"pId":110,"pName":"华安","Dog":{"dName":"小黄","color":"yellow"}},{"pId":120,"pName":"阿毛","Dog":{"dName":"阿毛","color":"red"}}]
    }
    public static void main(String[] args) {
        getJsonObjproperty();
    }
}