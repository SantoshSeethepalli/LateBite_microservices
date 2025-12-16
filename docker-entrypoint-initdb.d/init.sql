CREATE DATABASE latebite_auth_service;
CREATE DATABASE latebite_user_service;
CREATE DATABASE latebite_restaurant_service;
CREATE DATABASE latebite_cart_service;
CREATE DATABASE latebite_order_service;

GRANT ALL PRIVILEGES ON DATABASE latebite_restaurant_service TO common_user;
GRANT ALL PRIVILEGES ON DATABASE latebite_auth_service TO common_user;
GRANT ALL PRIVILEGES ON DATABASE latebite_user_service TO common_user;
GRANT ALL PRIVILEGES ON DATABASE latebite_cart_service TO common_user;
GRANT ALL PRIVILEGES ON DATABASE latebite_order_service TO common_user;