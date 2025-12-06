import ReactMarkdown from 'react-markdown';
import { Copy, Check } from 'lucide-react';
import { useState } from 'react';
import type { Block } from '../types';
import { clsx } from 'clsx';

interface PreviewPaneProps {
  blocks: Block[];
}

export function PreviewPane({ blocks }: PreviewPaneProps) {
  const [copied, setCopied] = useState(false);

  // Combine blocks into final text
  const combinedText = blocks
    .map((block) => {
        if (!block.title && !block.content) return '';
        const titlePart = block.title ? `## ${block.title}\n` : '';
        return `${titlePart}${block.content}`;
    })
    .filter(Boolean)
    .join('\n\n---\n\n');

  const handleCopy = async () => {
    try {
      await navigator.clipboard.writeText(combinedText);
      setCopied(true);
      setTimeout(() => setCopied(false), 2000);
    } catch (err) {
      console.error('Failed to copy:', err);
    }
  };

  return (
    <div className="flex flex-col h-full bg-zinc-900 border border-zinc-800 rounded-xl overflow-hidden shadow-sm">
      <div className="flex items-center justify-between px-4 py-3 border-b border-zinc-800 bg-zinc-950/30">
        <h2 className="text-sm font-semibold text-zinc-400 uppercase tracking-wider">Preview</h2>
        <button
          onClick={handleCopy}
          className={clsx(
            "flex items-center gap-2 px-3 py-1.5 rounded text-sm font-medium transition-all",
            copied
              ? "bg-green-500/10 text-green-400 hover:bg-green-500/20"
              : "bg-zinc-800 text-zinc-300 hover:bg-zinc-700 hover:text-white"
          )}
        >
          {copied ? <Check size={16} /> : <Copy size={16} />}
          {copied ? 'Copied!' : 'Copy to Clipboard'}
        </button>
      </div>

      <div className="flex-1 overflow-y-auto p-6 custom-scrollbar">
        {combinedText ? (
          <div className="prose prose-invert prose-sm max-w-none">
            <ReactMarkdown>{combinedText}</ReactMarkdown>
          </div>
        ) : (
          <div className="h-full flex items-center justify-center text-zinc-500 text-sm italic">
            Add content to see preview...
          </div>
        )}
      </div>
    </div>
  );
}
