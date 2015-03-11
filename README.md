coffeepot-bean-wr
=================

Coffeepot-bean-wr is a simple Java framework for marshalling Java beans to flat file and unmarshal flat file to Java Beans.

Features:

  - Support for delimited and fixed length formats.
  - Uses Java annotations for mapping.
  - Maps unannotated classes dynamically.
  - Override annotations dynamically.
  - Custom type handlers are supported.

Real examples of use you will find them in [coffeepot-br-sped-fiscal](https://github.com/jean-merelis/coffeepot-br-sped-fiscal) and [coffeepot-br-sintegra](https://github.com/jean-merelis/coffeepot-br-sintegra) projects. The coffeepot-br-sped-fiscal project uses delimited format, and the sintegra project uses FixedLength format.

Examples
--------

Mapping

```java
		@Record(fields = {
				@Field(name = "ID", id = true, constantValue = "ORDER"),
				@Field(name = "id"),
				@Field(name = "date"),
				@Field(name = "customer"),
				@Field(name = "items")
			})
		public class Order {

			private Integer id;
			private Date date;
			private String customer;
			private List<Item> items;
		
			// getters and setters...
		}

		@Record(fields = {
				@Field(name = "ID", id = true, constantValue = "ITEM"),
				@Field(name = "number"),
				@Field(name = "product"),
				@Field(name = "quantity")
			})
		public class Item {

			private int number;
			private String product;
			private double quantity;
		
			// getters ... setters...
		}		
```

Using...

```java

	public void test() throws Exception{
		Order order = new Order();
		//add some data

        File file = new File("ORDER.tmp");
        Writer w = new FileWriter(file);

        DelimitedWriter instance = new DelimitedWriter(w);
        instance.setDelimiter('|');
        instance.setEscape('\\');
        instance.setRecordTerminator("|\r\n");
        
        instance.write(order);

        w.flush();
        w.close();

```

Output: 

		ORDER|123|2015-03-10T00:04:15|john b|
		ITEM|1|product 1|10|
		ITEM|2|product 2|5|
		ITEM|3|product 3|2|

    
Overrides or mapping dynamically
--------------------------------

Json for Order class
```json
		{"fields" : [
		   {"name": "ID", "id" : true, "constantValue":"ORDER", "length":5},
		   {"name": "id", "length":5, "align":"RIGHT", "padding": "0"},
		   {"name": "date", "length": 8, "params":["ddMMyyyy"]},
		   {"name": "customer", "length":30},
		   {"name": "items"}
		]}
```
Json for Item class
```json
		{"fields" : [
		   {"name": "ID", "id" : true, "constantValue":"ITEM", "length":5},
		   {"name": "number", "length":3, "align":"RIGHT", "padding": "0"},
		   {"name": "product", "length": 30},
		   {"name": "quantity", "length":8, "align":"RIGHT", "padding": "0"}
		]}

```

```java
 public void overrideAnnotations() throws Exception {
        //Load a json or create a RecordModel object directly
        final String json = "{\"fields\" : [\n" +
"		   {\"name\": \"ID\", \"id\" : true, \"constantValue\":\"ORDER\", \"length\":5},\n" +
"		   {\"name\": \"id\", \"length\":5, \"align\":\"RIGHT\", \"padding\": \"0\"},\n" +
"		   {\"name\": \"date\", \"length\": 8, \"params\":[\"ddMMyyyy\"]},\n" +
"		   {\"name\": \"customer\", \"length\":30},\n" +
"		   {\"name\": \"items\"}\n" +
"		]}";
        final String jsonItem = "{\"fields\" : [\n" +
"		   {\"name\": \"ID\", \"id\" : true, \"constantValue\":\"ITEM\", \"length\":5},\n" +
"		   {\"name\": \"number\", \"length\":3, \"align\":\"RIGHT\", \"padding\": \"0\"},\n" +
"		   {\"name\": \"product\", \"length\": 30},\n" +
"		   {\"name\": \"quantity\", \"length\":8, \"align\":\"RIGHT\", \"padding\": \"0\"}\n" +
"		]}";

        //use a json framework of your choice
        final Gson gson = new Gson();

        Order order = new Order();

        // add some data

        File file = new File("ORDER_fixed.tmp");
        Writer w = new FileWriter(file);

        FixedLengthWriter instance = new FixedLengthWriter(w);
        instance.setRecordTerminator("\r\n");

        //implements a callback resolver
        instance.setCallback(new Callback<Class, RecordModel>() {
            @Override
            public RecordModel call(Class t) {
                if (Order.class.equals(t))
                    return gson.fromJson(json, RecordModel.class);
                if (Item.class.equals(t)){
                    return gson.fromJson(jsonItem, RecordModel.class);
                }
                return null;
            }
        });

        instance.write(order);

        w.flush();
        w.close();
    }	
```

output

```
ORDER0012310032015John B                        
ITEM 001Product1                      0010,000
ITEM 002Product 002                   0005,000
ITEM 003Product 003                   0002,000
```

