import { BlockList } from './components/BlockList';
import { PreviewPane } from './components/PreviewPane';
import { useBlocks } from './hooks/useBlocks';
import { Bot } from 'lucide-react';

function App() {
  const { blocks, setBlocks } = useBlocks();

  return (
    <div className="min-h-screen flex flex-col p-4 md:p-6 gap-6 max-w-[1600px] mx-auto">
      <header className="flex items-center gap-3 pb-2 border-b border-zinc-800">
        <div className="p-2 bg-indigo-500/10 rounded-lg text-indigo-400">
          <Bot size={28} />
        </div>
        <div>
          <h1 className="text-xl font-bold text-zinc-100 tracking-tight">AI System Prompt Composer</h1>
          <p className="text-xs text-zinc-500">Design and organize your system prompts efficiently</p>
        </div>
      </header>

      <main className="flex-1 grid grid-cols-1 lg:grid-cols-2 gap-6 min-h-0">
        <section className="flex flex-col min-h-[500px] lg:h-[calc(100vh-180px)]">
          <div className="mb-3 flex items-center justify-between">
            <h2 className="text-sm font-semibold text-zinc-400 uppercase tracking-wider">Editor</h2>
            <span className="text-xs text-zinc-600 px-2 py-1 bg-zinc-900 rounded border border-zinc-800">
              {blocks.length} Blocks
            </span>
          </div>
          <BlockList blocks={blocks} setBlocks={setBlocks} />
        </section>

        <section className="flex flex-col min-h-[500px] lg:h-[calc(100vh-180px)]">
          <div className="mb-3 hidden lg:block invisible">
            {/* Spacer to align with Editor header */}
            <h2 className="text-sm font-semibold">&nbsp;</h2>
          </div>
          <PreviewPane blocks={blocks} />
        </section>
      </main>
    </div>
  );
}

export default App;
