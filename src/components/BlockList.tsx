import {
  DndContext,
  closestCenter,
  KeyboardSensor,
  PointerSensor,
  useSensor,
  useSensors,
  type DragEndEvent,
} from '@dnd-kit/core';
import {
  arrayMove,
  SortableContext,
  sortableKeyboardCoordinates,
  verticalListSortingStrategy,
} from '@dnd-kit/sortable';
import type { Block } from '../types';
import { BlockCard } from './BlockCard';
import { Plus } from 'lucide-react';

interface BlockListProps {
  blocks: Block[];
  setBlocks: React.Dispatch<React.SetStateAction<Block[]>>;
}

export function BlockList({ blocks, setBlocks }: BlockListProps) {
  const sensors = useSensors(
    useSensor(PointerSensor),
    useSensor(KeyboardSensor, {
      coordinateGetter: sortableKeyboardCoordinates,
    })
  );

  function handleDragEnd(event: DragEndEvent) {
    const { active, over } = event;

    if (over && active.id !== over.id) {
      setBlocks((items) => {
        const oldIndex = items.findIndex((item) => item.id === active.id);
        const newIndex = items.findIndex((item) => item.id === over.id);
        return arrayMove(items, oldIndex, newIndex);
      });
    }
  }

  function addBlock() {
    const newBlock: Block = {
      id: crypto.randomUUID(),
      title: 'New Section',
      content: '',
    };
    setBlocks([...blocks, newBlock]);
  }

  function updateBlock(id: string, field: 'title' | 'content', value: string) {
    setBlocks(blocks.map(b => b.id === id ? { ...b, [field]: value } : b));
  }

  function removeBlock(id: string) {
    setBlocks(blocks.filter(b => b.id !== id));
  }

  return (
    <div className="flex flex-col h-full">
      <div className="flex-1 overflow-y-auto pr-2 pb-20">
        <DndContext
          sensors={sensors}
          collisionDetection={closestCenter}
          onDragEnd={handleDragEnd}
        >
          <SortableContext
            items={blocks}
            strategy={verticalListSortingStrategy}
          >
            {blocks.map((block) => (
              <BlockCard
                key={block.id}
                block={block}
                onUpdate={updateBlock}
                onRemove={removeBlock}
              />
            ))}
          </SortableContext>
        </DndContext>
      </div>

      <div className="pt-4 mt-auto border-t border-zinc-800 bg-zinc-950/80 backdrop-blur sticky bottom-0">
        <button
          onClick={addBlock}
          className="w-full flex items-center justify-center gap-2 py-3 bg-zinc-800 hover:bg-zinc-700 text-zinc-100 rounded-lg transition-colors font-medium border border-zinc-700"
        >
          <Plus size={20} />
          Add Block
        </button>
      </div>
    </div>
  );
}
