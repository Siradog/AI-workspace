import { useSortable } from '@dnd-kit/sortable';
import { CSS } from '@dnd-kit/utilities';
import type { Block } from '../types';
import { GripVertical, Trash2 } from 'lucide-react';
import { twMerge } from 'tailwind-merge';

interface BlockCardProps {
  block: Block;
  onUpdate: (id: string, field: 'title' | 'content', value: string) => void;
  onRemove: (id: string) => void;
}

export function BlockCard({ block, onUpdate, onRemove }: BlockCardProps) {
  const {
    attributes,
    listeners,
    setNodeRef,
    transform,
    transition,
    isDragging,
  } = useSortable({ id: block.id });

  const style = {
    transform: CSS.Transform.toString(transform),
    transition,
  };

  return (
    <div
      ref={setNodeRef}
      style={style}
      className={twMerge(
        'group bg-zinc-900 border border-zinc-800 rounded-lg p-4 mb-4 transition-colors',
        isDragging && 'opacity-50',
        'hover:border-zinc-700'
      )}
    >
      <div className="flex items-center justify-between mb-3">
        <div
          {...attributes}
          {...listeners}
          className="cursor-grab active:cursor-grabbing p-1 -ml-1 text-zinc-500 hover:text-zinc-300 rounded"
        >
          <GripVertical size={20} />
        </div>
        <input
          type="text"
          value={block.title}
          onChange={(e) => onUpdate(block.id, 'title', e.target.value)}
          placeholder="Block Title (e.g., Role)"
          className="flex-1 mx-2 bg-transparent border-none text-zinc-200 font-medium placeholder-zinc-600 focus:ring-0 focus:outline-none"
        />
        <button
          onClick={() => onRemove(block.id)}
          className="p-1 text-zinc-500 hover:text-red-400 rounded transition-colors"
          title="Remove block"
        >
          <Trash2 size={18} />
        </button>
      </div>
      <textarea
        value={block.content}
        onChange={(e) => onUpdate(block.id, 'content', e.target.value)}
        placeholder="Enter block content..."
        className="w-full bg-zinc-950/50 border border-zinc-800 rounded p-3 text-zinc-300 placeholder-zinc-600 focus:border-zinc-600 focus:ring-1 focus:ring-zinc-600 focus:outline-none min-h-[120px] resize-y text-sm font-mono leading-relaxed"
      />
    </div>
  );
}
