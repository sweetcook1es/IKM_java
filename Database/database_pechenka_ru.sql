-- (Покупатели)
CREATE TABLE IF NOT EXISTS customers (
    customer_id SERIAL PRIMARY KEY,
    full_name VARCHAR(50) NOT NULL,
    phone VARCHAR(20) UNIQUE
);

-- (Категории товаров)
CREATE TABLE IF NOT EXISTS categories (
    category_id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE,
    description TEXT
);

-- (Товары)
CREATE TABLE IF NOT EXISTS products (
    product_id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    price NUMERIC NOT NULL CHECK (price > 0),
    category_id INTEGER NOT NULL REFERENCES categories(category_id)
);

-- (Склады)
CREATE TABLE IF NOT EXISTS warehouses (
    warehouse_id SERIAL PRIMARY KEY,
    address VARCHAR(200) UNIQUE
);

-- (Складские остатки)
CREATE TABLE IF NOT EXISTS stock (
    stock_id SERIAL PRIMARY KEY,
    product_id INTEGER NOT NULL REFERENCES products(product_id),
    warehouse_id INTEGER NOT NULL REFERENCES warehouses(warehouse_id),
    quantity INTEGER NOT NULL CHECK (quantity >= 0),
    UNIQUE (product_id, warehouse_id)
);

-- (Статусы заказов)
CREATE TABLE IF NOT EXISTS order_statuses (
    status_id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE
);

-- (Сотрудники)
CREATE TABLE IF NOT EXISTS employees (
    employee_id SERIAL PRIMARY KEY,
    personnel_number VARCHAR(20) UNIQUE,
    full_name VARCHAR(50) NOT NULL,
    role VARCHAR(50) NOT NULL CHECK (role IN ('Администратор', 'Комплектовщик', 'Оператор', 'Аналитик'))
);

-- (Заказы)
CREATE TABLE IF NOT EXISTS orders (
    order_id SERIAL PRIMARY KEY,
    creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status_id INTEGER NOT NULL REFERENCES order_statuses(status_id),
    customer_id INTEGER NOT NULL REFERENCES customers(customer_id)
);

-- (Состав заказа)
CREATE TABLE IF NOT EXISTS order_items (
    order_item_id SERIAL PRIMARY KEY,
    order_id INTEGER NOT NULL REFERENCES orders(order_id),
    product_id INTEGER NOT NULL REFERENCES products(product_id),
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    price_at_order NUMERIC NOT NULL CHECK (price_at_order > 0),
    UNIQUE (order_id, product_id)
);

-- (Складские операции)
CREATE TABLE IF NOT EXISTS warehouse_operations (
    operation_id SERIAL PRIMARY KEY,
    order_id INTEGER NOT NULL REFERENCES orders(order_id),
    employee_id INTEGER NOT NULL REFERENCES employees(employee_id),
    operation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- (Отчеты)
CREATE TABLE IF NOT EXISTS reports (
    report_id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    generation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


INSERT INTO order_statuses (name) VALUES 
('Заказ создан'),
('Собирается'),
('Собран'),
('Готов к выдаче'),
('Выполнен');