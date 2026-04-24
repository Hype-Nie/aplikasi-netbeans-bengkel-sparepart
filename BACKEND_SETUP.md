# Backend Setup (Laragon + MySQL + phpMyAdmin)

## 1. Start service di Laragon

- Jalankan Laragon.
- Start `Apache` dan `MySQL`.

## 2. Import database

- Buka `http://localhost/phpmyadmin`.
- Pilih menu `Import`.
- Import file:
  - `database/bengkel_sparepart.sql`

Setelah import berhasil, database `bengkel_sparepart` dan seluruh tabel akan otomatis dibuat.

## 3. Akun login default aplikasi

- Username: `admin`
- Password: `admin123`

## 4. Konfigurasi koneksi database di aplikasi

Aplikasi membaca konfigurasi dari environment variable atau JVM property berikut:

- `APP_DB_HOST` (default: `localhost`)
- `APP_DB_PORT` (default: `3306`)
- `APP_DB_NAME` (default: `bengkel_sparepart`)
- `APP_DB_USER` (default: `root`)
- `APP_DB_PASS` (default: string kosong)

Contoh menjalankan via JVM args:

```
-DAPP_DB_HOST=localhost -DAPP_DB_PORT=3306 -DAPP_DB_NAME=bengkel_sparepart -DAPP_DB_USER=root -DAPP_DB_PASS=
```

## 5. Catatan penting

- Struktur aplikasi sudah diubah ke pola MVC:
  - `model`: representasi data
  - `dao`: akses database JDBC
  - `view`: komponen UI Swing
  - `controller`: alur interaksi UI dan backend
- Modul yang sudah terkoneksi backend:
  - Login admin
  - Kelola data pelanggan
  - Kelola data sparepart
  - Kelola data supplier
  - Transaksi servis masuk
  - Transaksi pembelian supplier
  - Dashboard ringkasan dari database
