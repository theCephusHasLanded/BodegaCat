import api from './api';

const FundraiserService = {
  getAllFundraisers: async () => {
    try {
      const response = await api.get('/fundraisers');
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  getFundraiserById: async (id) => {
    try {
      const response = await api.get(`/fundraisers/${id}`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  createFundraiser: async (fundraiserData) => {
    try {
      const response = await api.post('/fundraisers', fundraiserData);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  makeDonation: async (fundraiserId, donationData) => {
    try {
      const response = await api.post(
        `/fundraisers/${fundraiserId}/donate`, 
        donationData
      );
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  getFundraiserDonations: async (fundraiserId) => {
    try {
      const response = await api.get(`/fundraisers/${fundraiserId}/donations`);
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  getUserDonations: async () => {
    try {
      const response = await api.get('/fundraisers/user/donations');
      return response.data;
    } catch (error) {
      throw error;
    }
  }
};

export default FundraiserService;