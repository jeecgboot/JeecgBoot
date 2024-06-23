export default jest.fn().mockImplementation(() => ({
  postMessage: jest.fn(),
  onmessage: jest.fn(),
  onerror: jest.fn(),
}));
