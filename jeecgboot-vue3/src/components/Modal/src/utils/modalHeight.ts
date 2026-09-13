/**
 * Viewport-aware modal body height helpers.
 * Used by ModalWrapper so long content scrolls and footer buttons stay on screen.
 * Fixes #9850
 */

/** Parse a CSS pixel value; ignore percent/auto/empty so antd centered `top: 50%` is not treated as 50px. */
export function parseCssPx(value?: string | null): number | null {
  if (!value) {
    return null;
  }
  const trimmed = value.trim();
  if (!trimmed.endsWith('px')) {
    return null;
  }
  const n = Number.parseFloat(trimmed);
  return Number.isFinite(n) ? n : null;
}

/**
 * Resolve the modal's visual offset from the top of the viewport.
 * Prefer a concrete CSS `top` in px (stable during enter animation); otherwise use the painted rect.
 */
export function resolveModalTop(cssTop?: string | null, rectTop = 0, fallback = 100): number {
  const parsed = parseCssPx(cssTop);
  if (parsed != null && parsed > 0) {
    return parsed;
  }
  if (rectTop > 1) {
    return rectTop;
  }
  return fallback;
}

export interface ModalBodyMaxHeightOptions {
  viewportHeight: number;
  modalTop: number;
  headerHeight: number;
  footerHeight: number;
  footerOffset?: number;
  extraGap?: number;
}

/**
 * Max height for the modal body/content area so header + footer remain inside the viewport.
 * Bottom gap mirrors the top offset (antd default 100px) with a 24px floor.
 */
export function computeModalBodyMaxHeight(opts: ModalBodyMaxHeightOptions): number {
  const bottomGap = Math.max(opts.modalTop, 24);
  const extra = (opts.footerOffset || 0) + (opts.extraGap || 0);
  let maxHeight =
    opts.viewportHeight - opts.modalTop - bottomGap - opts.headerHeight - opts.footerHeight - extra;
  // Original fudge: very small top offset tends to produce a window scrollbar.
  if (opts.modalTop < 40) {
    maxHeight -= 26;
  }
  return Math.max(Math.floor(maxHeight), 100);
}
