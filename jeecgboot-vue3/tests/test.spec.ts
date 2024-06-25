// import { mount } from '@vue/test-utils';
// import { Button } from '/@/components/Button';

test('if jest is normal.', async () => {
  expect('jest').toEqual('jest');
});

// TODO Vue component testing is not supported temporarily
// test('is a Vue instance.', async () => {
//   const wrapper = mount(Button, {
//     slots: {
//       default: 'Button text',
//     },
//   });
//   expect(wrapper.html()).toContain('Button text');
// });
