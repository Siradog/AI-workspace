import { useEffect, useState } from 'react';
import type { Block } from '../types';

const STORAGE_KEY = 'prompt-composer-blocks';

const INITIAL_BLOCKS: Block[] = [
  { id: '1', title: 'Role', content: 'You are a helpful AI assistant.' },
  { id: '2', title: 'Task', content: 'Please help me write a blog post about React.' },
];

export function useBlocks() {
  const [blocks, setBlocks] = useState<Block[]>(() => {
    const saved = localStorage.getItem(STORAGE_KEY);
    return saved ? JSON.parse(saved) : INITIAL_BLOCKS;
  });

  useEffect(() => {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(blocks));
  }, [blocks]);

  return { blocks, setBlocks };
}
