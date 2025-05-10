import api from './api';

const MembershipService = {
  getCurrentMembership: async () => {
    try {
      const response = await api.get('/memberships/current');
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  getMembershipBenefits: async () => {
    try {
      const response = await api.get('/memberships/benefits');
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  subscribeRegular: async () => {
    try {
      const response = await api.post('/memberships/subscribe/regular', {});
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  subscribeChampion: async () => {
    try {
      const response = await api.post('/memberships/subscribe/champion', {});
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  cancelMembership: async () => {
    try {
      const response = await api.delete('/memberships/cancel');
      return response.data;
    } catch (error) {
      throw error;
    }
  }
};

export default MembershipService;