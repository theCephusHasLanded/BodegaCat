import api from './api';

const BodegaService = {
  getAllBodegaStores: async () => {
    try {
      const response = await api.get('/bodegas');
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  getBodegaStoreById: async (id) => {
    try {
      const response = await api.get(`/bodegas/${id}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  createBodegaStore: async (storeData) => {
    try {
      const response = await api.post('/bodegas', storeData);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  getBodegaStoreCats: async (storeId) => {
    try {
      const response = await api.get(`/bodegas/${storeId}/cats`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  addCatToBodegaStore: async (storeId, catData) => {
    try {
      const response = await api.post(
        `/bodegas/${storeId}/cats`, 
        catData
      );
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  recordBodegaVisit: async (storeId, visitData) => {
    try {
      const response = await api.post(
        `/bodegas/${storeId}/visits`, 
        visitData
      );
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  getBodegaVisits: async (storeId) => {
    try {
      const response = await api.get(`/bodegas/${storeId}/visits`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  getUserBodegaVisits: async () => {
    try {
      const response = await api.get('/bodegas/user/visits');
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  getBodegaStoresByNeighborhood: async (neighborhood) => {
    try {
      const response = await api.get(`/bodegas/neighborhood/${neighborhood}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  getAllNeighborhoods: async () => {
    try {
      const response = await api.get('/bodegas/neighborhoods');
      return response.data;
    } catch (error) {
      throw error;
    }
  }
};

export default BodegaService;