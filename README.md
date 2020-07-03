# h2 Smile
Spring data jap 学习

1、环境：Java jdk 1.8 + Spring Boot 2.1.4 + Spring data-jpa + h2 数据库

2、包含 Spring Data JPA 常用 CRUD 操作汇总，以及 Spring Data JPA Specification（规范）实现复杂查询


EasyExcel 类是个空类，方法全部继承于 EasyExcelFactory，目的是编码起来名称更加简短直观，常用方法如下：

====读取 excel 文件方法====
1、可以读取本地 excel 文件，也可以网络文件（比如文件上传），网络文件使用 InputStream 参数即可。
2、pathName、file、inputStream 参数都是需要读取的 excel 文件。
3、head：是一个实体对象，将实体对象的属性与 excel 文件的列进行对应，一个实体对象就是 excel 中的一行数据。
4、readListener 是读取监听器，每读取一行标题、或者一行正文数据，都会进入相应的回调方法，然后可以对数据进行处理。
ExcelReaderBuilder read(String pathName) 
ExcelReaderBuilder read(String pathName, Class head, ReadListener readListener)
ExcelReaderBuilder read(String pathName, ReadListener readListener)
ExcelReaderBuilder read(File file)
ExcelReaderBuilder read(File file, Class head, ReadListener readListener)
ExcelReaderBuilder read(File file, ReadListener readListener)
ExcelReaderBuilder read(InputStream inputStream)
ExcelReaderBuilder read(InputStream inputStream, Class head, ReadListener readListener)
ExcelReaderBuilder read(InputStream inputStream, ReadListener readListener)


====写入 excel 文件方法====
1、可以写入到本地文件，也可以是网络文件（如文件下载），网络文件使用 OutputStream 参数即可。
2、head：是一个实体对象，将实体对象的属性与 excel 文件的列进行对应，一个实体对象就是 excel 中的一行数据。
ExcelWriterBuilder write(String pathName) 
ExcelWriterBuilder write(String pathName, Class head)
ExcelWriterBuilder write(File file)
ExcelWriterBuilder write(File file, Class head)
ExcelWriterBuilder write(OutputStream outputStream)
ExcelWriterBuilder write(OutputStream outputStream, Class head)

ExcelWriterSheetBuilder writerSheet(String sheetName)：创建 excel 工作簿写入构造器，同时指定工作簿名称
ExcelWriterTableBuilder writerTable(Integer tableNo)：创建 WriterTable 表格写入构造器，一个 WriterSheet 工作簿可以有多个 WriterTable 表格

====ExcelWriterBuilder - excel 写构造器，用于设置写入操作====
1、sheet 方法返回的是 ExcelWriterSheetBuilder 对象，同理这是一个设置 excel 中 sheet 写入的构造器。
ExcelWriterSheetBuilder sheet(String sheetName)：一个 excel 中可以有多页，一页就是一个 sheet，这里是指定 sheet 的名称，默认为 Sheet1、Sheet2...
ExcelWriterSheetBuilder sheet(Integer sheetNo)：设置 sheet 的序号，从 0 开始，比如写入到第 3 页，则 sheetNo 为 2。
ExcelWriterSheetBuilder sheet(Integer sheetNo, String sheetName)：同时指定 sheet 序号与名称。

autoCloseStream(Boolean autoCloseStream)：设置是否自动关闭流，默认为 true，设置成 false 后，需要自己手动关流。
ExcelWriter build()：构建 ExcelWriter 写对象。
excludeColumnFiledNames(Collection<String> excludeColumnFiledNames)：写入的时候，指定忽略的列，即指定哪些属性不写入到 excel 文件中去.
includeColumnFiledNames(Collection<String> includeColumnFiledNames)：只输出自定义的列
head(List<List<String>> head)：指定写入时的表头/表头，使用列表指定
head(Class clazz)：指定写入时的表头/表头，使用实体对象指定
registerWriteHandler(WriteHandler writeHandler)：自定义写处理器，如自定义样式

====ExcelReaderBuilder - excel 写构造器，用于设置写入操作====
1、sheet 方法返回的是 ExcelReaderSheetBuilder 对象，同理这是一个设置 excel 中 sheet 读取的构造器。
ExcelReaderSheetBuilder sheet(String sheetName)：读取指定名称的 sheet。
ExcelReaderSheetBuilder sheet(Integer sheetNo)：读取指定序号的 sheet，从 0 开始。
ExcelReaderSheetBuilder sheet(Integer sheetNo, String sheetName)：读取指定序号和名称的 sheet。



2、设置模板文件，比如写入的时候，希望将数据写入到一个模板文件中去，支持本地文件和网络文件。
withTemplate(String pathName)
withTemplate(File templateFile)
withTemplate(InputStream templateInputStream)


===ExcelWriterSheetBuilder - 写入 sheet 构造器，用于设置 sheet===
1、doWrite(List data)：将数据 data 写入到 excel 中的 sheet 中，内部会调用 finish 方法结束操作，关闭流，释放资源，外部可以不用再处理。
2、WriteSheet build() ：构建 WriteSheet 工作簿写对象.
3、needHead(Boolean needHead) ：工作簿是否需要写入表头，默认为 true.

===ExcelReaderSheetBuilder - 读取 sheet 构造器，用于设置 sheet===
1、void doRead()：读取 excel 文件内容，不返回内容，需要使用读取监听器进行监听回调处理读取的数据，以及读取异常的处理。
2、List<T> doReadSync()：同步读取返回结果，将读取的最终内容进行返回。


===常用注解====

@ExcelIgnore
@ColumnWidth
@ExcelProperty 标识实体中的属性与 excel 文件中的列相对应，常用属性如下：
	index：表示下标匹配，从0开始，0 应表格中的第1列的值，1 对应 excel 中第二列的值，依此类推
	value：表示名称匹配，直接指明对应表中的哪一列，值是一个字符串数组，按顺序为一级标题、二级标题，一级标题相同时，会自动合并居中。
	官方不建议 index 和 name 同时使用，同一个对象中，要么只用 index，要么只用 name。

@ExcelIgnore：标识在字段上，表示忽略这个字段不与 excel 中的类匹配。




===AnalysisEventListener - excel 读取事件监听器===
1、AnalysisEventListener 抽象类实现了 ReadListener 接口，
2、自定义的读取监听器需要实现此抽象类，然后实现 invoke 方法，
3、读取监听器的目的在于能捕获读取到的每一条数据，这样可以轻松的对每一条数据进行处理，比如存库、清洗、验证、记录日志等
4、读取监听器实例不能交由 spring 容器管理，EasyExcel.read 时只能通过 new 读取监听器对象，如果读取监听器中需要用到哪个 bean，则可以通过构造器参数传入。
5、invoke ：每成功解析一条数据，都会进入此方法
6、invokeHeadMap：每成功解析完成一行表头，都会进入此方法
7、doAfterAllAnalysed：所有数据解析完成后进入此方法
8、onException：解析异常时进入本方法，如果抛出异常，则停止读取，否则继续读取下一行




==== ExcelWriter 写对象====
1、此工具用于通过 POI 将值写入到 Excel，可以执行以下两个功能：
---创建一个新的空 Excel 工作簿，在值填充后将值写入流。
---编辑现有 Excel，编写原始 Excel 文件，或将其写入其他位置。
ExcelWriter write(List data, WriteSheet writeSheet)：将数据（data）写入到工作簿（writeSheet）中
ExcelWriter write(List data, WriteSheet writeSheet, WriteTable writeTable)：将数据（data）写入到工作簿（writeSheet）的表格（WriteTable）中
void finish()：结束操作，关闭 I/O 流，是否资源。 

===ExcelWriterTableBuilder 表格构造对象===
1、needHead(Boolean needHead)：表格是否设置表头/标题，默认为 true.



