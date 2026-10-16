# E-Commerce Store (Full-Stack)

Full-stack online prodavnica slična manjoj verziji Amazona.

## Tehnologije

- **Frontend**: React + TypeScript + Vite
- **Backend**: Java 17 + Spring Boot 3 + Spring Security + JWT
- **Skladištenje**: JSON fajlovi (bez SQL baze)
- **Autentifikacija**: JWT

## Funkcionalnosti

### Kupac
- Registracija i prijava
- Pregled proizvoda i kategorija
- Pretraga i filtriranje (cena, kategorija)
- Shopping cart
- Checkout i kreiranje porudžbine
- Istorija porudžbina
- Wishlist
- Ocene i recenzije

### Administrator
- CRUD proizvoda
- Upravljanje kategorijama
- Pregled i promena statusa porudžbina
- Praćenje zaliha (automatski se ažurira pri porudžbini)

## Pokretanje

### Backend
```bash
cd backend
mvn spring-boot:run
```
Backend radi na `http://localhost:8080`

Podaci se čuvaju u `backend/data/` folderu (users.json, products.json, itd.).

**Admin nalog** (kreira se automatski):
- Username: `admin`
- Password: `admin123`

### Frontend
```bash
cd frontend
npm install
npm run dev
```
Frontend radi na `http://localhost:5173`

## Struktura

```
ecommerce-store/
├── backend/          # Spring Boot aplikacija
│   ├── data/         # JSON fajlovi (kreira se automatski)
│   └── src/...
└── frontend/         # React aplikacija
    └── src/
        ├── components/
        ├── context/
        ├── pages/
        ├── services/
        └── types/
```

## Napomene

- Ovo je demo aplikacija. Za produkciju treba dodati validaciju, bolje rukovanje greškama, HTTPS, itd.
- Lozinke se hešuju sa BCrypt.
- Zalihe se automatski smanjuju pri kreiranju porudžbine.
- Sample podaci se učitavaju pri prvom pokretanju.

