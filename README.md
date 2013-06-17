coffeepot-bean-wr
=================

Coffeepot-bean-wr is a simple Java framework for marshalling Java beans to flat file.

Features: 
  - Support for delimited and fixed length formats.
  - Uses Java annotations for mapping.
  - Custom type handlers are supported.
	
	
Download jar file: 
	https://sourceforge.net/projects/coffeepotbeanwr/
	
An example:

	//Mapping a class
	@Record( 
	  fields = {
	    @Field(name = "", constantValue = "PERSON"),
	    @Field(name = "name"),
	    @Field(name = "age"),
	    @Field(name = "birth", params = {"dd/MM/yyyy"}),
	    @Field(name = "children"),
	    @Field(name = "", getter = "childrenCount", classType = Integer.class)
	})
	public class Person{
		private String name;
		private int age;
		private Date birth;
		private List<Child> children;
		
		//.. getters and setters
		
	    public Integer childrenCount() {
	        if (children == null) {
	            return 0;
	        }
	        return children.size();
	    }	
	}
	
	@Record(fields = {
	    @Field(name = "", constantValue = "CHILD"),
	    @Field(name = "name", params = {"CharCase.UPPER"}),
	    @Field(name = "age")
	})
	public class Child {
	    private String name;
	    private int age;
		
		//... getters and setters
	}
	
	//Test Class
	public class Test{
		public static void main(String[] args) {
			Writer w = new FileWriter("C:\\TEST_DELIMITED.TXT");
			DelimitedWriter objWriter = new DelimitedWriter(w);
			objWriter.setDelimiter(';');	
			objWriter.setRecordTerminator("\r\n");
			
			List<Child> children = new LinkedList<>();
			Child child = new Child();
			child.setName("John");
			child.setAge(14);
			children.add(child);
	
			child = new Child();
			child.setName("Ana");
			child.setAge(11);
			children.add(child);
			
			Person person = new Person();
			person.setName("Jack");
			person.setAge(35);
			person.setBirth(new Date());
			person.setChildren(children);
			
			objWriter.write(person);
			objWriter.flush();
			objWriter.close();	
		}
	}
	
	Output:
	
	PERSON;Jack;35;01/01/1978
	CHILD;JOHN;14
	CHILD;ANA;11
	2
