import { TreeItem } from '/@/components/Tree/index';

export const treeData: TreeItem[] = [
  {
    title: 'parent ',
    key: '0-0',
    children: [
      { title: 'leaf', key: '0-0-0' },
      {
        title: 'leaf',
        key: '0-0-1',
        children: [
          { title: 'leaf', key: '0-0-0-0', children: [{ title: 'leaf', key: '0-0-0-0-1' }] },
          { title: 'leaf', key: '0-0-0-1' },
        ],
      },
    ],
  },
  {
    title: 'parent 2',
    key: '1-1',
    children: [
      { title: 'leaf', key: '1-1-0' },
      { title: 'leaf', key: '1-1-1' },
    ],
  },
  {
    title: 'parent 3',
    key: '2-2',
    children: [
      { title: 'leaf', key: '2-2-0' },
      { title: 'leaf', key: '2-2-1' },
    ],
  },
];
