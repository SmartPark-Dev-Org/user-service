-- ============================================================
-- SmartPark :: user-service — Seed Data Reference
-- ============================================================
-- NOTE: Actual seeding is performed at startup by DataInitializer.java
-- (dev.smartpark.userservice.config.DataInitializer) which uses
-- BCryptPasswordEncoder(strength=12) to hash passwords at runtime.
-- This file serves as a human-readable reference of the seed data.
-- ============================================================

-- Seed 1: Root user
-- username : root
-- password : Manu2006   (BCrypt-encoded at runtime)
-- email    : root@smartpark.dev
-- role     : ROLE_ROOT

-- Seed 2: Admin user 1
-- username : admin1
-- password : Admin@2006  (BCrypt-encoded at runtime)
-- email    : admin1@smartpark.dev
-- role     : ROLE_ADMIN

-- Seed 3: Admin user 2
-- username : admin2
-- password : Admin@2006  (BCrypt-encoded at runtime)
-- email    : admin2@smartpark.dev
-- role     : ROLE_ADMIN
