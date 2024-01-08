# Laboratory work 9

## Program Functionality

In this lab, we will consider the basics of Java IO, learn how to interact with the web API, and learn how to create
Excel files in the Java environment.

## Phase 1: Using the specified API

### I user https://fakestoreapi.com/

```java

@Data
@NoArgsConstructor
@ToString
public class Product {
    private long id;
    private String title;
    private double price;
    private String description;
    private String category;
    private String image;
    private Rating rating;

    @Data
    @NoArgsConstructor
    @ToString
    public static class Rating {
        private double rate;
        private int count;
    }
}

@Data
@NoArgsConstructor
@ToString
public class User {
    private long id;
    private String email;
    private String username;
    private String password;
    private Name name;
    private String phone;
    private Address address;
    private int __v;

    @Data
    @NoArgsConstructor
    @ToString
    public static class Name {
        private String firstname;
        private String lastname;

    }

    @Data
    @NoArgsConstructor
    @ToString
    public static class Address {
        private Geolocation geolocation;
        private String city;
        private String street;
        private int number;
        private String zipcode;

    }

    @Data
    @NoArgsConstructor
    @ToString
    public static class Geolocation {
        private String lat;
        @JsonProperty("long")
        private String lon;

    }
}

```

```java

@Service
public class FakeStoreApiConnection<T> {

    private final WebClient.Builder webclintBuilder;

    @Value("${api.path.base}")
    private String basePath;

    public FakeStoreApiConnection(WebClient.Builder webclintBuilder) {
        this.webclintBuilder = webclintBuilder;
    }

    public List<T> getListFromApi(String endpoint, Class<T> elementType) {
        ParameterizedTypeReference<List<T>> typeReference = new ParameterizedTypeReference<>() {
            @Override
            @NonNull
            public Type getType() {
                return new ParameterizedType() {
                    @Override
                    public Type[] getActualTypeArguments() {
                        return new Type[]{elementType};
                    }

                    @Override
                    public Type getRawType() {
                        return List.class;
                    }

                    @Override
                    public Type getOwnerType() {
                        return null;
                    }
                };
            }
        };

        return webclintBuilder.baseUrl(basePath)
                .build()
                .get()
                .uri(endpoint)
                .retrieve()
                .bodyToMono(typeReference)
                .blockOptional()
                .orElseThrow();
    }
}

@Service
public class ProductService {

    private final FakeStoreApiConnection<Product> faceStoreApiConnect;

    private final ProductRepository productRepository;

    @Value("${api.path.products}")
    private String productsPath;

    public ProductService(FakeStoreApiConnection<Product> faceStoreApiConnect, ProductRepository productRepository) {
        this.faceStoreApiConnect = faceStoreApiConnect;
        this.productRepository = productRepository;
    }

    @PostConstruct
    public void saveToExcel() {
        List<Product> usersFromApi = getProductsPathFromApi();
        productRepository.saveAll(usersFromApi);
    }

    public List<Product> getProductsPathFromApi() {
        return faceStoreApiConnect.getListFromApi(productsPath, Product.class);
    }
}

@Service
public class UserService {

    private final FakeStoreApiConnection<User> faceStoreApiConnect;

    private final UserRepository userRepository;

    @Value("${api.path.users}")
    private String usersPath;

    public UserService(FakeStoreApiConnection<User> faceStoreApiConnect, UserRepository userRepository) {
        this.faceStoreApiConnect = faceStoreApiConnect;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void saveToExcel() {
        List<User> usersFromApi = getUsersFromApi();
        userRepository.saveAll(usersFromApi);
    }

    public List<User> getUsersFromApi() {
        return faceStoreApiConnect.getListFromApi(usersPath, User.class);
    }
}

```

## Phase 2: Save the received data in Excel format.

### Find the 10 hottest and coldest days by average temperature.

```java
public class ExcelModelBinder {


    public static Map<Integer, Object[]> bindUserData(List<User> users) {

        Map<Integer, Object[]> data = new TreeMap<>();
        int rowCont = 1;

        data.put(rowCont++, new Object[]{"ID", "Email", "Username", "Password", "First Name", "Last Name", "Phone", "City", "Street", "Number", "Zipcode", "Latitude", "Longitude", "__v"});

        for (User user : users) {
            data.put(rowCont++, new Object[]{
                    user.getId(),
                    user.getEmail(),
                    user.getUsername(),
                    user.getPassword(),
                    user.getName().getFirstname(),
                    user.getName().getLastname(),
                    user.getPhone(),
                    user.getAddress().getCity(),
                    user.getAddress().getStreet(),
                    user.getAddress().getNumber(),
                    user.getAddress().getZipcode(),
                    user.getAddress().getGeolocation().getLat(),
                    user.getAddress().getGeolocation().getLon(),
                    user.get__v()
            });
        }

        return data;
    }

    public static Map<Integer, Object[]> bindProductData(List<Product> products) {

        Map<Integer, Object[]> data = new TreeMap<>();
        int rowCont = 1;

        data.put(rowCont++, new Object[]{"ID", "Title", "Price", "Description", "Category", "Image", "Rating Rate", "Rating Count"});

        for (Product product : products) {
            data.put(rowCont++, new Object[]{
                    product.getId(),
                    product.getTitle(),
                    product.getPrice(),
                    product.getDescription(),
                    product.getCategory(),
                    product.getImage(),
                    product.getRating().getRate(),
                    product.getRating().getCount()
            });
        }
        return data;
    }
}

```

```java

@Component
public class ProductRepository {

    private final ExcelService excelService;

    public ProductRepository(ExcelService excelService) {
        this.excelService = excelService;
    }

    public void saveAll(List<Product> products) {
        excelService.writeToExcel(ExcelModelBinder.bindProductData(products), "product", "Product");
    }
}

@Component
public class UserRepository {

    private final ExcelService excelService;

    public UserRepository(ExcelService excelService) {
        this.excelService = excelService;
    }

    public void saveAll(List<User> users) {
        excelService.writeToExcel(ExcelModelBinder.bindUserData(users), "user", "User");
    }
}

@Service
@Slf4j
public class ExcelService {


    public void writeToExcel(Map<Integer, Object[]> data, String sheetList, String filename) {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetList);

        Set<Integer> keyset = data.keySet();

        int rownum = 0;
        for (Integer key : keyset) {

            Row row = sheet.createRow(rownum++);
            Object[] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr) {
                Cell cell = row.createCell(cellnum++);

                if (obj instanceof String string) {
                    cell.setCellValue(string);
                } else if (obj instanceof Integer integer) {
                    cell.setCellValue(integer);
                } else if (obj instanceof Double doubleValue) {
                    cell.setCellValue(doubleValue);
                } else if (obj instanceof Float floatValue) {
                    cell.setCellValue(floatValue);
                } else if (obj instanceof Long longValue) {
                    cell.setCellValue(longValue);
                } else if (obj instanceof Boolean booleanValue) {
                    cell.setCellValue(booleanValue);
                } else if (obj instanceof Date date) {
                    cell.setCellValue(date);
                }

            }
        }

        try {
            log.info("Start try to write to file {}", filename);
            FileOutputStream out = new FileOutputStream(filename + ".xlsx");
            workbook.write(out);
            out.close();
            log.info("Successfully written to file {}", filename);
        } catch (Exception e) {
            log.error("Error: {} Message: {}", e.getClass(), e.getMessage());
        }
    }
}
```

## Phase 3: Cover jUnit code with tests.

```java

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    public void getUsersFromApi() {
        assertNotNull(productService.getProductsPathFromApi());
    }

    @Test
    public void saveToExcel() {
        productService.saveToExcel();
    }

}

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void getUsersFromApi() {
        assertNotNull(userService.getUsersFromApi());
    }

    @Test
    public void saveToExcel() {
        userService.saveToExcel();
    }

}

```

# Conclusion

In the course of studying the basics of Java IO and interacting with web APIs, we managed to gain a deeper understanding
of working with the file system and interacting with web servers. Learning the process of creating Excel files turned
out to be useful for working with tabular data. The acquired skills expand our potential to develop programs that use
input-output facilities and interaction with external services. Overall, this experience improved our Java programming
skills, making our projects more colorful and functional.

