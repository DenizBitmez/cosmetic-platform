
# Cosmetic Platform

Bu projede bir kozmetik platformu oluşturularak bazı işlemler yapılması amaçlanmıştır. Projede üç rol bulunmaktadır.

Uzman: Ürün içeriklerini girebilir, yorum yapabilir ve puanlama verebilir.

Müşteri: Ürün deneyimlerini paylaşmak için yorum yapabilir ve puanlama verebilir.

Admin: Platformun genel yönetimini yapabilir, kullanıcı ve ürün işlemlerini düzenleyebilir.

## Projede Bulunan Özellikler

Ürün Yönetimi: Uzmanlar ürünlerin içerik bilgilerini girebilir ve düzenleyebilir.

Yorum ve Puanlama: Hem uzmanlar hem de müşteriler ürünler için yorum yapabilir, puanlama verebilir, yorumlarını güncelleyebilir veya silebilir.

Yetkilendirme: Her rol için belirli yetkilendirmeler yapılmıştır. Örneğin, sadece uzmanlar ürün içeriklerini girebilir, admin ise platformun genel yönetimini yapar.

REST API: Ürün, kullanıcı, yorum ve puanlama işlemleri için RESTful API servisleri sunulmaktadır.

Veritabanı işlemleri: Spring Data JPA kullanılarak CRUD işlemleri yapıldı.

## Kullanılan Teknolojiler

Java Spring Boot: Restful Apı kullanmak için eklendi.

MySQL: Veritabanı yönetim sistemi olarak kullanılmıştır.

JWT: Kimlik doğrulama ve yetkilendirme işlemleri için JSON Web Token kullanılmıştır.

Docker: Geliştirme ortamını containerize etmek için kullanılmıştır.

JUnit ve Mockito: Birim testler için kullanıldı.
Kurulum

## Gereksinimler

Java 17 veya daha üstü

Maven

MySQL

Docker (Opsiyonel)

## Adımlar

### 1. Proje dosyalarını klonlayın:


`git clone https://github.com/kullaniciadi/kozmetik-platformu.git
cd kozmetik-platformu
`
### 2. Veritabanı yapılandırması:

MySQL üzerinde bir veritabanı oluşturun:

`CREATE DATABASE kozmetik_platformu;
`

`src/main/resources/application.properties` dosyasındaki veritabanı bağlantı ayarlarını güncelleyin:

`spring.datasource.url=jdbc:mysql://localhost:3306/kozmetik_platformu 

spring.datasource.username=user

spring.datasource.password=password`

### 3. Projeyi derleyin ve çalıştırın:

`mvn clean install

mvn spring-boot:run
`

### 4. API'leri test edin:
Proje ayağa kalktıktan sonra, aşağıdaki örnek URL'leri kullanarak Postman veya benzeri bir araçla API'leri test edebilirsiniz:

**Ürün Ekleme** (Sadece uzmanlar):

`POST /api/product
`

**Yorum Ekleme**:

`POST /api/product/{productId}/comment
`

## Docker ile Çalıştırma (Opsiyonel)

Projeyi Docker ile çalıştırmak isterseniz:

### 1. Docker container'ını oluşturun ve çalıştırın:

`docker-compose up --build
`
### 2. Docker ile veritabanı tabloları otomatik olarak yüklenecektir.

## API Kullanımı

### 1. Ürün İşlemleri

**Ürün Ekle** (Sadece Uzman)

`POST /api/product/add
`

**Ürün Güncelleme**

``PUT /api/product/update/{id}`

**Ürün Silme**

`DELETE /api/product/delete/{id}`

**Ürün Detayları Getir**

`GET /api/product/{id}
`

### 2. Yorum ve Puanlama İşlemleri

**Yorum Ekle**

`POST /api/product/{id}/comment`

**Puanlama Ekle**

`POST /api/product/{id}/
`

**Yorum Güncelle**

`PUT /api/product/id}/comment/{id}`

**Yorum Sil**

`DELETE /api/product/{id}/comment/{id}
`

### 3. Kullanıcı Yönetimi

**Kullanıcı Kaydı**

`POST /api/user/register
`

Kullanıcı rolüne göre (uzman, müşteri, admin) kayıt işlemi yapılır.

**Kullanıcı Girişi**

`POST /api/user/login
`

**Kullanıcı Bilgilerini Güncelle**

`PUT /api/user/{id}
`

**Kullanıcı Bilgilerini Getir**

`GET /api/user/{id}
`

### 4. Admin İşlemleri

**Kullanıcı Listesi Getir**

`GET /api/admin/user`

**Kullanıcı Sil**

`DELETE /api/admin/user/{id}
`

**Ürün Sil (Sadece Admin)**

`DELETE /api/product/{id}
`

## Testler

Projede birim testler ve entegrasyon testleri mevcuttur. Testleri çalıştırmak için:

`mvn test
`

Testler, uygulamanın farklı katmanlarında (service, repository, controller) yazılmış olup, Mockito ve JUnit ile geliştirilmiştir.

## Katkıda Bulunma

Projeye katkıda bulunmak isterseniz aşağıdaki adımları izleyebilirsiniz:

Projeyi fork edin.

Yeni bir branch oluşturun.

Değişikliklerinizi yapın.

Pull request oluşturun.

## Lisans

Bu proje MIT lisansı altında lisanslanmıştır. Daha fazla bilgi için LICENSE dosyasına göz atabilirsiniz.







