import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { toast } from 'react-toastify';
import { FiPackage, FiClock, FiCheck, FiX, FiTruck, FiRefreshCw } from 'react-icons/fi';
import orderService from '../services/orderService';
import './Orders.css';

const Orders = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [activeTab, setActiveTab] = useState('buyer'); // 'buyer' or 'seller'
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  useEffect(() => {
    fetchOrders();
  }, [activeTab, page]);

  const fetchOrders = async () => {
    setLoading(true);
    try {
      let response;
      if (activeTab === 'buyer') {
        response = await orderService.getMyOrders(page, 10);
      } else {
        response = await orderService.getSellerOrders(page, 10);
      }
      
      // Handle both paginated and array responses
      if (response.content) {
        setOrders(response.content);
        setTotalPages(response.totalPages || 1);
      } else if (Array.isArray(response)) {
        setOrders(response);
        setTotalPages(1);
      } else {
        setOrders([]);
        setTotalPages(1);
      }
    } catch (error) {
      console.error('Error fetching orders:', error);
      // Show empty state instead of error for no orders
      setOrders([]);
      setTotalPages(1);
    } finally {
      setLoading(false);
    }
  };

  const handleCancelOrder = async (orderId) => {
    if (!window.confirm('Are you sure you want to cancel this order?')) return;
    
    try {
      await orderService.cancelOrder(orderId);
      toast.success('Order cancelled successfully');
      fetchOrders();
    } catch (error) {
      toast.error('Failed to cancel order');
    }
  };

  const handleUpdateStatus = async (orderId, status) => {
    try {
      await orderService.updateOrderStatus(orderId, status);
      toast.success(`Order status updated to ${status}`);
      fetchOrders();
    } catch (error) {
      toast.error('Failed to update order status');
    }
  };

  const getStatusIcon = (status) => {
    switch (status?.toUpperCase()) {
      case 'PENDING':
        return <FiClock className="status-icon pending" />;
      case 'CONFIRMED':
        return <FiCheck className="status-icon confirmed" />;
      case 'SHIPPED':
      case 'IN_TRANSIT':
        return <FiTruck className="status-icon shipped" />;
      case 'DELIVERED':
        return <FiCheck className="status-icon delivered" />;
      case 'CANCELLED':
        return <FiX className="status-icon cancelled" />;
      default:
        return <FiPackage className="status-icon" />;
    }
  };

  const getStatusClass = (status) => {
    switch (status?.toUpperCase()) {
      case 'PENDING':
        return 'status-pending';
      case 'CONFIRMED':
        return 'status-confirmed';
      case 'SHIPPED':
      case 'IN_TRANSIT':
        return 'status-shipped';
      case 'DELIVERED':
        return 'status-delivered';
      case 'CANCELLED':
        return 'status-cancelled';
      default:
        return '';
    }
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  const formatPrice = (price) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD'
    }).format(price || 0);
  };

  return (
    <div className="orders-page">
      <div className="orders-header">
        <h1><FiPackage /> Orders</h1>
        <button className="refresh-btn" onClick={fetchOrders}>
          <FiRefreshCw /> Refresh
        </button>
      </div>

      <div className="orders-tabs">
        <button
          className={`tab-btn ${activeTab === 'buyer' ? 'active' : ''}`}
          onClick={() => { setActiveTab('buyer'); setPage(0); }}
        >
          My Purchases
        </button>
        <button
          className={`tab-btn ${activeTab === 'seller' ? 'active' : ''}`}
          onClick={() => { setActiveTab('seller'); setPage(0); }}
        >
          My Sales
        </button>
      </div>

      {loading ? (
        <div className="loading-container">
          <div className="spinner"></div>
          <p>Loading orders...</p>
        </div>
      ) : orders.length === 0 ? (
        <div className="empty-state">
          <FiPackage className="empty-icon" />
          <h2>No Orders Found</h2>
          <p>
            {activeTab === 'buyer'
              ? "You haven't placed any orders yet."
              : "You haven't received any orders yet."}
          </p>
          {activeTab === 'buyer' && (
            <Link to="/marketplace" className="browse-btn">
              Browse Marketplace
            </Link>
          )}
        </div>
      ) : (
        <>
          <div className="orders-list">
            {orders.map((order) => (
              <div key={order.id} className="order-card">
                <div className="order-header">
                  <div className="order-id">
                    <span className="label">Order #</span>
                    <span className="value">{order.id?.slice(0, 8) || 'N/A'}</span>
                  </div>
                  <div className={`order-status ${getStatusClass(order.status)}`}>
                    {getStatusIcon(order.status)}
                    <span>{order.status || 'PENDING'}</span>
                  </div>
                </div>

                <div className="order-body">
                  <div className="order-info">
                    <div className="info-row">
                      <span className="label">Date:</span>
                      <span className="value">{formatDate(order.createdAt || new Date())}</span>
                    </div>
                    {order.listing && (
                      <div className="info-row">
                        <span className="label">Product:</span>
                        <span className="value">{order.listing.title}</span>
                      </div>
                    )}
                    <div className="info-row">
                      <span className="label">Quantity:</span>
                      <span className="value">{order.quantity || 1} {order.unit || 'units'}</span>
                    </div>
                    <div className="info-row">
                      <span className="label">Total:</span>
                      <span className="value total">{formatPrice(order.totalAmount)}</span>
                    </div>
                  </div>

                  {order.shippingAddress && (
                    <div className="shipping-info">
                      <span className="label">Shipping to:</span>
                      <p>{order.shippingAddress}</p>
                    </div>
                  )}
                </div>

                <div className="order-actions">
                  {activeTab === 'buyer' && order.status === 'PENDING' && (
                    <button
                      className="cancel-btn"
                      onClick={() => handleCancelOrder(order.id)}
                    >
                      <FiX /> Cancel Order
                    </button>
                  )}
                  
                  {activeTab === 'seller' && (
                    <>
                      {order.status === 'PENDING' && (
                        <button
                          className="confirm-btn"
                          onClick={() => handleUpdateStatus(order.id, 'CONFIRMED')}
                        >
                          <FiCheck /> Confirm
                        </button>
                      )}
                      {order.status === 'CONFIRMED' && (
                        <button
                          className="ship-btn"
                          onClick={() => handleUpdateStatus(order.id, 'SHIPPED')}
                        >
                          <FiTruck /> Mark Shipped
                        </button>
                      )}
                    </>
                  )}
                </div>
              </div>
            ))}
          </div>

          {totalPages > 1 && (
            <div className="pagination">
              <button
                disabled={page === 0}
                onClick={() => setPage(p => p - 1)}
              >
                Previous
              </button>
              <span>Page {page + 1} of {totalPages}</span>
              <button
                disabled={page >= totalPages - 1}
                onClick={() => setPage(p => p + 1)}
              >
                Next
              </button>
            </div>
          )}
        </>
      )}
    </div>
  );
};

export default Orders;
