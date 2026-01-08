import { marketplaceApi } from './api';

const marketplaceService = {
  // Get all listings with pagination and filters
  getListings: async (params = {}) => {
    const { page = 0, size = 12, category, minPrice, maxPrice, search, sortBy } = params;
    const response = await marketplaceApi.get('/listings', {
      params: { page, size, category, minPrice, maxPrice, search, sortBy }
    });
    return response.data;
  },

  // Get listing by ID
  getListingById: async (id) => {
    const response = await marketplaceApi.get(`/listings/${id}`);
    return response.data;
  },

  // Create a new listing (farmers only)
  createListing: async (listingData) => {
    const response = await marketplaceApi.post('/listings', listingData);
    return response.data;
  },

  // Update listing
  updateListing: async (id, listingData) => {
    const response = await marketplaceApi.put(`/listings/${id}`, listingData);
    return response.data;
  },

  // Delete listing
  deleteListing: async (id) => {
    const response = await marketplaceApi.delete(`/listings/${id}`);
    return response.data;
  },

  // Get my listings (for farmers)
  getMyListings: async (page = 0, size = 10) => {
    const response = await marketplaceApi.get('/listings/my', {
      params: { page, size }
    });
    return response.data;
  },

  // Get all categories
  getCategories: async () => {
    const response = await marketplaceApi.get('/categories');
    return response.data;
  },

  // Search listings
  searchListings: async (query, page = 0, size = 12) => {
    const response = await marketplaceApi.get('/listings/search', {
      params: { query, page, size }
    });
    return response.data;
  },
};

export default marketplaceService;
