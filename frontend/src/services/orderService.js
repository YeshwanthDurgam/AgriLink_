import { orderApi } from './api';

const orderService = {
  // Get all orders for the current user
  getMyOrders: async (page = 0, size = 10) => {
    const response = await orderApi.get('/orders/my', {
      params: { page, size }
    });
    return response.data;
  },

  // Get order by ID
  getOrderById: async (id) => {
    const response = await orderApi.get(`/orders/${id}`);
    return response.data;
  },

  // Create a new order
  createOrder: async (orderData) => {
    const response = await orderApi.post('/orders', orderData);
    return response.data;
  },

  // Update order status (for farmers/sellers)
  updateOrderStatus: async (id, status) => {
    const response = await orderApi.patch(`/orders/${id}/status`, { status });
    return response.data;
  },

  // Cancel order
  cancelOrder: async (id) => {
    const response = await orderApi.post(`/orders/${id}/cancel`);
    return response.data;
  },

  // Get orders for seller (farmers)
  getSellerOrders: async (page = 0, size = 10) => {
    const response = await orderApi.get('/orders/seller', {
      params: { page, size }
    });
    return response.data;
  },
};

export default orderService;
