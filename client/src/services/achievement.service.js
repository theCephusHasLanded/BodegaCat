import api from './api';

const AchievementService = {
  getAllAchievements: async () => {
    try {
      const response = await api.get('/achievements');
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  getAchievementCategories: async () => {
    try {
      const response = await api.get('/achievements/categories');
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  getUserAchievements: async () => {
    try {
      const response = await api.get('/achievements/user');
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  getUserAchievementProgress: async () => {
    try {
      const response = await api.get('/achievements/user/progress');
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  getUserPassportPoints: async () => {
    try {
      const response = await api.get('/achievements/user/points');
      return response.data;
    } catch (error) {
      throw error;
    }
  },

  getAchievementLeaderboard: async () => {
    try {
      const response = await api.get('/achievements/leaderboard');
      return response.data;
    } catch (error) {
      throw error;
    }
  }
};

export default AchievementService;