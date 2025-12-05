# LateBite Food Delivery Platform - Complete Frontend API Documentation

## ⚠️ Implementation Status Legend
- ✅ **Implemented** - Endpoint exists and tested
- ⚠️ **Partially Implemented** - Exists but needs modifications
- ❌ **Not Implemented** - Needs backend development

---

## Base Configuration
- **API Base URL:** `http://localhost:8080`
- **Auth Header:** `Authorization: Bearer <token>`
- **Auto-injected Headers by Gateway:** `X-User-Id`, `X-Ref-Id`, `X-Role`, `X-Phone`

---

## 🔐 Authentication Flow

### 1. Send OTP ✅
**Endpoint:** `POST /auth/send-otp`

**Request:**
```json
{
  "phone": "9876543210",
  "role": "USER" // or "RESTAURANT"
}
```

**Response:**
```json
{
  "status": "otp sent to 9876543210"
}
```

---

### 2. Verify OTP ✅
**Endpoint:** `POST /auth/verify-otp`

**Request:**
```json
{
  "phone": "9876543210",
  "role": "USER",
  "otp": "123456"
}
```

**Response:**
```json
{
  "detailsRequired": true,
  "authUserId": 123,
  "accessToken": "eyJhbGc...",
  "refreshToken": "uuid-string",
  "role": "USER",
  "phone": "9876543210"
}
```

---

### 3. Complete Profile ✅
**Endpoint:** `POST /auth/complete-profile`

**Request (User):**
```json
{
  "authUserId": 123,
  "type": "USER",
  "payload": {
    "username": "John Doe",
    "profilePhoto": "https://example.com/photo.jpg"
  }
}
```

**Request (Restaurant):**
```json
{
  "authUserId": 123,
  "type": "RESTAURANT",
  "payload": {
    "username": "Pizza Palace",
    "profilePhoto": "https://example.com/logo.jpg",
    "upiId": "restaurant@upi"
  }
}
```

**Response:**
```json
{
  "accessToken": "eyJhbGc...",
  "refreshToken": "uuid-string",
  "role": "USER",
  "phone": "9876543210"
}
```

---

### 4. Token Refresh ✅
**Endpoint:** `POST /auth/renew-refresh`

**Request:**
```json
{
  "refreshToken": "uuid-string"
}
```

**Response:**
```json
{
  "accessToken": "new-access-token",
  "refreshToken": "new-refresh-token"
}
```

---

### 5. Logout ✅
**Endpoint:** `POST /auth/logout`

**Request:**
```json
{
  "authUserId": 123
}
```

**Response:**
```json
{
  "status": "logged_out"
}
```

---

### 6. Admin Login ✅
**Endpoint:** `POST /auth/admin/login`

**Request:**
```json
{
  "phone": "9988776655",
  "otp": "123456"
}
```

**Response:** Same as verify OTP

---

## 🍕 Restaurant Browsing (Public/User)

### 1. Browse All Restaurants ❌ NOT IMPLEMENTED
**Endpoint:** `GET /api/restaurant/browse`

**Headers:** None (public) or `Authorization: Bearer <token>`

**Response:**
```json
[
  {
    "id": 1,
    "username": "Pizza Palace",
    "phoneNumber": "9876543210",
    "profilePhoto": "https://example.com/logo.jpg",
    "createdAt": "2024-01-01T00:00:00",
    "updatedAt": "2024-01-15T10:00:00"
  }
]
```

**⚠️ WORKAROUND:** Use admin endpoint `GET /api/restaurant/all` (requires ADMIN role)

---

### 2. Get Restaurant Details ❌ NOT IMPLEMENTED
**Endpoint:** `GET /api/restaurant/{restaurantId}`

**Headers:** None (public) or `Authorization: Bearer <token>`

**Response:**
```json
{
  "id": 1,
  "username": "Pizza Palace",
  "phoneNumber": "9876543210",
  "upiId": "restaurant@upi",
  "profilePhoto": "https://example.com/logo.jpg",
  "createdAt": "2024-01-01T00:00:00",
  "updatedAt": "2024-01-15T10:00:00"
}
```

---

### 3. Get Restaurant Menu ❌ NOT IMPLEMENTED
**Endpoint:** `GET /api/restaurant/{restaurantId}/menu`

**Headers:** None (public) or `Authorization: Bearer <token>`

**Response:**
```json
[
  {
    "id": 1,
    "itemPhoto": "https://example.com/pizza.jpg",
    "itemName": "Margherita Pizza",
    "description": "Classic cheese and tomato",
    "unitPrice": 199.00,
    "isAvailable": true,
    "createdAt": "2024-01-01T00:00:00"
  }
]
```

**⚠️ NOTE:** This is critical for the ordering flow. Backend implementation required.

---

## 🛒 Cart Management

### 1. Add or Update Cart Item ✅
**Endpoint:** `PUT /api/cartItem/addOrUpdate`

**Headers:** `Authorization: Bearer <token>`

**Request:**
```json
{
  "cartId": 123,        // null or omit for new cart
  "userId": 456,
  "restaurantId": 789,
  "itemId": 101
}
```

**Query Parameters:**
- `increaseQuantity` (boolean, default: true) - true to increment, false to decrement

**Response:**
```json
{
  "id": 123,
  "userId": 456,
  "restaurantId": 789,
  "totalAmount": 499.50,
  "cartItems": [
    {
      "id": 1,
      "itemId": 101,
      "quantity": 2,
      "createdAt": "2024-01-15T10:00:00"
    }
  ],
  "createdAt": "2024-01-15T10:00:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

**Usage Examples:**
- **Add item:** `PUT /api/cartItem/addOrUpdate?increaseQuantity=true`
- **Remove one:** `PUT /api/cartItem/addOrUpdate?increaseQuantity=false`

---

### 2. Get Cart Details ✅
**Endpoint:** `GET /api/cart/getCartDetails/cartId?cartId={cartId}`

**Headers:** `Authorization: Bearer <token>`

**Response:**
```json
{
  "userId": 123,
  "restaurantId": 456,
  "totalAmount": 499.50,
  "orderedItems": [
    {
      "itemId": 789,
      "itemName": "Margherita Pizza",
      "quantity": 2,
      "unitPrice": 199.00,
      "totalPrice": 398.00
    }
  ]
}
```

**⚠️ IMPORTANT:** Cart is automatically deleted after retrieval! Call only before placing order.

---

### 3. Delete Cart Item ❌ NOT IMPLEMENTED
**Endpoint:** `DELETE /api/cartItem/{cartItemId}`

**Headers:** `Authorization: Bearer <token>`

**Response:** `200 OK`

**⚠️ WORKAROUND:** Use `PUT /api/cartItem/addOrUpdate?increaseQuantity=false` repeatedly until quantity reaches 0.

---

### 4. Get Active Cart ❌ NOT IMPLEMENTED
**Endpoint:** `GET /api/cart/active`

**Headers:** `Authorization: Bearer <token>`

**Response:** Same as Get Cart Details

**Use Case:** Retrieve user's current cart without knowing cartId

---

## 📦 Order Management

### 1. Place Order ✅
**Endpoint:** `POST /api/order/place`

**Headers:** `Authorization: Bearer <token>`

**Request:**
```json
{
  "cartId": 123,
  "utrNumber": 123456789012
}
```

**Response:** `201 Created`

---

### 2. View Past Orders ✅
**Endpoint:** `GET /api/order/pastOrders`

**Headers:** `Authorization: Bearer <token>`

**Response:**
```json
[
  {
    "id": 1,
    "userId": 123,
    "restaurantId": 456,
    "orderStatus": "DELIVERED",
    "totalAmount": 499.50,
    "utrNumber": 123456789012,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T11:00:00",
    "orderItems": [
      {
        "itemId": 789,
        "itemName": "Margherita Pizza",
        "quantity": 2,
        "unitPrice": 199.00,
        "totalPrice": 398.00
      }
    ]
  }
]
```

---

### 3. Cancel Order ✅
**Endpoint:** `GET /api/order/{orderId}/cancel`

**Headers:** `Authorization: Bearer <token>`

**Response:** `200 OK "Order cancelled successfully"`

---

### 4. Estimated Delivery Time ✅
**Endpoint:** `GET /api/order/{orderId}/estimated-time`

**Headers:** `Authorization: Bearer <token>`

**Response:**
```json
42
```
(Time in minutes)

---

## 💳 Payment

### Generate QR Code ✅
**Endpoint:** `GET /api/payment/generate-qr?restaurantUpiId={UPI}&amount={AMOUNT}`

**Headers:** `Authorization: Bearer <token>`

**Response:** PNG Image (binary)

**Example:**
```
GET /api/payment/generate-qr?restaurantUpiId=restaurant@upi&amount=499.50
```

---

## 🏪 Restaurant Features

### 1. Get Restaurant Profile ❌ NOT IMPLEMENTED
**Endpoint:** `GET /api/restaurant/profile`

**Headers:** `Authorization: Bearer <token>` (Role: RESTAURANT)

**Response:**
```json
{
  "id": 1,
  "username": "Pizza Palace",
  "phoneNumber": "9876543210",
  "upiId": "restaurant@upi",
  "profilePhoto": "https://example.com/logo.jpg",
  "createdAt": "2024-01-01T00:00:00",
  "updatedAt": "2024-01-15T10:00:00"
}
```

---

### 2. Update Restaurant Profile ✅
**Endpoint:** `PATCH /api/restaurant/update`

**Headers:** `Authorization: Bearer <token>` (Role: RESTAURANT)

**Request:**
```json
{
  "name": "Pizza Palace",
  "phoneNumber": "9876543210",
  "upiId": "restaurant@upi"
}
```

**Response:** `200 OK "Profile Updated"`

---

### 3. Add Menu Item ✅
**Endpoint:** `POST /api/MenuItem/addItem`

**Headers:** `Authorization: Bearer <token>` (Role: RESTAURANT)

**Request:**
```json
{
  "itemPhoto": "https://example.com/pizza.jpg",
  "itemName": "Margherita Pizza",
  "description": "Classic cheese and tomato",
  "unitPrice": 199.00
}
```

**Response:** `201 Created "Added"`

---

### 4. Delete Menu Item ✅
**Endpoint:** `DELETE /api/MenuItem/remove/{menuItemId}`

**Headers:** `Authorization: Bearer <token>` (Role: RESTAURANT)

**Response:** `200 OK "Deleted"`

---

### 5. Toggle Item Availability ✅
**Endpoint:** `PUT /api/MenuItem/{menuItemId}/toggle-availability`

**Headers:** `Authorization: Bearer <token>` (Role: RESTAURANT)

**Response:** `200 OK "Request processed"`

---

### 6. View All Orders ✅
**Endpoint:** `GET /api/order/restaurant/allOrders?status={STATUS}`

**Headers:** `Authorization: Bearer <token>` (Role: RESTAURANT)

**Query Parameters:**
- `status` (optional): Filter by order status

**Order Statuses:**
- `AWAITING_VERIFICATION`
- `CONFIRMED_AND_PREPARING`
- `READY_FOR_PICKUP`
- `PICKED_UP`
- `DELIVERED`
- `CANCELLED`

**Response:** Array of orders (same structure as user's past orders)

---

### 7. Update Order Status ✅
**Endpoint:** `PATCH /api/order/{orderId}/updateStatus?updatedStatus={STATUS}`

**Headers:** `Authorization: Bearer <token>` (Role: RESTAURANT)

**Response:** `200 OK`

---

## 👨‍💼 Admin Features

### 1. Create Restaurant ✅
**Endpoint:** `POST /api/restaurant/create`

**Headers:** `Authorization: Bearer <token>` (Role: ADMIN)

**Request:**
```json
{
  "username": "Pizza Palace",
  "profilePhoto": "https://example.com/logo.jpg",
  "phone": "9876543210",
  "upiId": "restaurant@upi"
}
```

**Response:**
```json
123
```
(Restaurant ID)

---

### 2. Get All Restaurants ✅
**Endpoint:** `GET /api/restaurant/all`

**Headers:** `Authorization: Bearer <token>` (Role: ADMIN)

**Response:**
```json
[
  {
    "id": 1,
    "username": "Pizza Palace",
    "phoneNumber": "9876543210",
    "upiId": "restaurant@upi",
    "profilePhoto": "https://example.com/logo.jpg",
    "createdAt": "2024-01-01T00:00:00",
    "updatedAt": "2024-01-15T10:00:00"
  }
]
```

---

### 3. Delete Restaurant ✅
**Endpoint:** `DELETE /api/restaurant/{id}`

**Headers:** `Authorization: Bearer <token>` (Role: ADMIN)

**Response:** `200 OK "Deleted"`

---

## 👤 User Profile

### 1. Get User Profile ❌ NOT IMPLEMENTED
**Endpoint:** `GET /api/user/profile`

**Headers:** `Authorization: Bearer <token>` (Role: USER)

**Response:**
```json
{
  "id": 123,
  "username": "John Doe",
  "phoneNumber": "9876543210",
  "profilePhoto": "https://example.com/photo.jpg",
  "createdAt": "2024-01-01T00:00:00"
}
```

---

### 2. Update User Profile ❌ NOT IMPLEMENTED
**Endpoint:** `PATCH /api/user/profile`

**Headers:** `Authorization: Bearer <token>` (Role: USER)

**Request:**
```json
{
  "username": "John Doe Updated",
  "profilePhoto": "https://example.com/new-photo.jpg"
}
```

**Response:** `200 OK "Profile Updated"`

---

## 🔄 Order Status Flow

```
AWAITING_VERIFICATION 
    ↓
CONFIRMED_AND_PREPARING 
    ↓
READY_FOR_PICKUP 
    ↓
PICKED_UP 
    ↓
DELIVERED

(CANCELLED can happen at any stage before PICKED_UP)
```

---

## 🚨 Critical Backend TODOs

### Priority 1 (Blocks MVP)
1. ❌ `GET /api/restaurant/browse` - Browse restaurants
2. ❌ `GET /api/restaurant/{id}` - Get restaurant details
3. ❌ `GET /api/restaurant/{restaurantId}/menu` - Get restaurant menu
4. ❌ `GET /api/cart/active` - Get user's active cart
5. ❌ `DELETE /api/cartItem/{cartItemId}` - Remove cart item

### Priority 2 (User Experience)
6. ❌ `GET /api/user/profile` - Get user profile
7. ❌ `PATCH /api/user/profile` - Update user profile
8. ❌ `GET /api/restaurant/profile` - Get restaurant profile (for owners)

### Priority 3 (Nice to Have)
9. ❌ Search/filter restaurants
10. ❌ Search menu items
11. ❌ Reviews and ratings
12. ❌ Order tracking with WebSocket
13. ❌ Notifications

---

## 📝 TypeScript Interfaces

```typescript
interface AuthResponse {
  detailsRequired: boolean;
  authUserId: number;
  accessToken: string;
  refreshToken: string;
  role: 'USER' | 'RESTAURANT' | 'ADMIN';
  phone: string;
}

interface Restaurant {
  id: number;
  username: string;
  phoneNumber: string;
  upiId: string;
  profilePhoto: string;
  createdAt: string;
  updatedAt: string;
}

interface MenuItem {
  id: number;
  itemPhoto: string;
  itemName: string;
  description: string;
  unitPrice: number;
  isAvailable: boolean;
  createdAt: string;
}

interface Cart {
  id: number;
  userId: number;
  restaurantId: number;
  totalAmount: number;
  cartItems: CartItem[];
  createdAt: string;
  updatedAt: string;
}

interface CartItem {
  id: number;
  itemId: number;
  quantity: number;
  createdAt: string;
}

interface CartDetailsResponse {
  userId: number;
  restaurantId: number;
  totalAmount: number;
  orderedItems: OrderItemDTO[];
}

interface OrderItemDTO {
  itemId: number;
  itemName: string;
  quantity: number;
  unitPrice: number;
  totalPrice: number;
}

interface Order {
  id: number;
  userId: number;
  restaurantId: number;
  orderStatus: OrderStatus;
  totalAmount: number;
  utrNumber: number;
  createdAt: string;
  updatedAt: string;
  orderItems: OrderItemDTO[];
}

type OrderStatus = 
  | 'AWAITING_VERIFICATION'
  | 'CONFIRMED_AND_PREPARING'
  | 'READY_FOR_PICKUP'
  | 'PICKED_UP'
  | 'DELIVERED'
  | 'CANCELLED';
```

---

## 🔧 Axios Configuration

```typescript
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 10000,
});

// Request interceptor
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('accessToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Response interceptor for token refresh
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        const refreshToken = localStorage.getItem('refreshToken');
        const { data } = await axios.post('http://localhost:8080/auth/renew-refresh', {
          refreshToken,
        });

        localStorage.setItem('accessToken', data.accessToken);
        localStorage.setItem('refreshToken', data.refreshToken);

        originalRequest.headers.Authorization = `Bearer ${data.accessToken}`;
        return api(originalRequest);
      } catch (refreshError) {
        // Logout user
        localStorage.clear();
        window.location.href = '/login';
        return Promise.reject(refreshError);
      }
    }

    return Promise.reject(error);
  }
);

export default api;
```

---

## 📱 Error Handling

```typescript
interface ApiError {
  status: number;
  message: string;
  timestamp: string;
}

// Common error codes
const ERROR_CODES = {
  UNAUTHORIZED: 401,       // Invalid/expired token
  FORBIDDEN: 403,          // Insufficient permissions
  NOT_FOUND: 404,          // Resource not found
  CONFLICT: 409,           // Business logic error
  INTERNAL_ERROR: 500,     // Server error
};
```

---

## 🎨 UI Component Checklist

### Authentication
- [ ] Login/Signup modal
- [ ] OTP input component
- [ ] Profile completion form
- [ ] Token refresh mechanism
- [ ] Protected routes

### User Interface
- [ ] Restaurant listing cards
- [ ] Restaurant detail page with menu
- [ ] Menu item card
- [ ] Cart sidebar/modal
- [ ] Checkout flow with QR payment
- [ ] Order history list
- [ ] Order tracking/status page

### Restaurant Dashboard
- [ ] Order management table with filters
- [ ] Menu item CRUD interface
- [ ] Status update controls
- [ ] Restaurant profile settings

### Admin Dashboard
- [ ] Restaurant management table
- [ ] Create restaurant form
- [ ] System overview (optional)

### Shared Components
- [ ] Navigation bar (role-based)
- [ ] Loading states
- [ ] Toast notifications
- [ ] Confirmation dialogs
- [ ] Image upload widget
- [ ] Error boundaries

---

## 🚀 Implementation Phases

### Phase 1: MVP Backend Fixes (2-3 days)
1. Implement restaurant browsing endpoints
2. Implement menu viewing endpoints
3. Fix cart management (delete item)
4. Implement user profile endpoints

### Phase 2: Authentication (2 days)
1. Login/OTP flow
2. Token management
3. Protected routes
4. Role-based access

### Phase 3: User Features (5-7 days)
1. Restaurant browsing
2. Menu viewing
3. Cart management
4. Checkout with QR
5. Order history
6. Order tracking

### Phase 4: Restaurant Dashboard (3-4 days)
1. Order management
2. Menu management
3. Profile settings

### Phase 5: Admin Panel (2-3 days)
1. Restaurant CRUD
2. System monitoring

### Phase 6: Polish (2-3 days)
1. Responsive design
2. Error handling
3. Loading states
4. Performance optimization

---

**Total Estimated Time:** 16-22 days for complete implementation

---

## 📞 Support

For questions or issues, contact the backend team about missing endpoints.

**Backend Repository:** `backend-microservices`  
**Current Branch:** `development`

---

**Last Updated:** December 5, 2025
