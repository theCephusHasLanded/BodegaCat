import api from './api';

const ProductService = {
  getAllProducts: async () => {
    return api.get('/products');
  },

  getProductById: async (id) => {
    return api.get(`/products/${id}`);
  },

  createProduct: async (productData) => {
    return api.post('/products', productData);
  },

  updateProduct: async (id, productData) => {
    return api.put(`/products/${id}`, productData);
  },

  deleteProduct: async (id) => {
    return api.delete(`/products/${id}`);
  },

  // Benefits-specific methods
  getSnapEligibleProducts: async () => {
    return api.get('/products/benefits/snap');
  },

  getProductsByBenefitsCategory: async (category) => {
    return api.get(`/products/benefits/${category}`);
  },

  getProductsByDiscountProgram: async (programCode) => {
    return api.get(`/products/benefits/program/${programCode}`);
  },

  // File upload for product image
  uploadProductImage: async (file) => {
    const formData = new FormData();
    formData.append('file', file);
    
    return api.post('/upload/product-image', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
  }
};

export default ProductService;