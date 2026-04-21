import API from './axios';

export const getComplaints = () => API.get('/complaints');
export const createComplaint = (data) => API.post('/complaints', data);
export const getComplaintById = (id) => API.get(`/complaints/${id}`);