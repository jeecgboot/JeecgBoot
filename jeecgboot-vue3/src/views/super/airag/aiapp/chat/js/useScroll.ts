import type { Ref } from 'vue';
import { nextTick, ref, watch, onBeforeUnmount } from 'vue';

type ScrollElement = HTMLDivElement | null;

interface ScrollReturn {
  scrollRef: Ref<ScrollElement>;
  scrollToBottom: () => Promise<void>;
  scrollToTop: () => Promise<void>;
  scrollToBottomIfAtBottom: () => Promise<void>;
  scrollToBottomIfAuto: () => Promise<void>;
}

export function useScroll(): ScrollReturn {
  const scrollRef = ref<ScrollElement>(null);
  // update-begin--author:liaozhiyang---date:20261027---for：【LHZP-1351】AI生成过程中用户向上滚动时不再强制滚动到底部
  const autoScroll = ref(true);
  // 距离底部小于该值视为在底部
  const bottomThreshold = 30;
  // 是否正在拖动滚动条
  let dragging = false;
  let lastScrollTop = 0;
  let touchStartY = 0;
  const getDistanceToBottom = (el: HTMLDivElement) => el.scrollHeight - el.scrollTop - el.clientHeight;
  // 内容可滚动时才暂停，避免内容未溢出时误暂停导致后续不再跟随
  const pauseAutoScroll = () => {
    const el = scrollRef.value;
    if (el && el.scrollHeight > el.clientHeight) autoScroll.value = false;
  };
  // 鼠标滚轮/触控板向上滚动：在滚动发生前直接暂停（避免平滑滚动动画被流式输出打断）
  const handleWheel = (e: WheelEvent) => {
    if (e.deltaY < 0) pauseAutoScroll();
  };
  // 移动端手指下滑（内容向上）：暂停
  const handleTouchStart = (e: TouchEvent) => {
    touchStartY = e.touches[0]?.clientY ?? 0;
  };
  const handleTouchMove = (e: TouchEvent) => {
    if ((e.touches[0]?.clientY ?? 0) > touchStartY) pauseAutoScroll();
  };
  // 拖动滚动条期间不自动滚动（点击滚动条时 target 为容器本身）
  const handlePointerDown = (e: PointerEvent) => {
    if (e.target === scrollRef.value) {
      dragging = true;
      window.addEventListener('pointerup', handlePointerUp, { once: true });
    }
  };
  const handlePointerUp = () => {
    dragging = false;
    const el = scrollRef.value;
    if (el) autoScroll.value = getDistanceToBottom(el) <= bottomThreshold;
  };
  const handleScroll = () => {
    const el = scrollRef.value;
    if (!el) return;
    const scrollTop = el.scrollTop;
    const distanceToBottom = getDistanceToBottom(el);
    if (scrollTop < lastScrollTop && distanceToBottom > bottomThreshold) {
      // 向上滚动离开底部（键盘、拖动滚动条等）
      autoScroll.value = false;
    } else if (scrollTop >= lastScrollTop && distanceToBottom <= bottomThreshold && !dragging) {
      // 向下滚动回到底部，恢复跟随
      autoScroll.value = true;
    }
    lastScrollTop = scrollTop;
  };
  const listeners: [string, any][] = [
    ['scroll', handleScroll],
    ['wheel', handleWheel],
    ['touchstart', handleTouchStart],
    ['touchmove', handleTouchMove],
    ['pointerdown', handlePointerDown],
  ];
  watch(scrollRef, (el, oldEl) => {
    listeners.forEach(([type, fn]) => {
      oldEl?.removeEventListener(type, fn);
      el?.addEventListener(type, fn, { passive: true });
    });
  });
  onBeforeUnmount(() => {
    listeners.forEach(([type, fn]) => scrollRef.value?.removeEventListener(type, fn));
    window.removeEventListener('pointerup', handlePointerUp);
  });

  // 强制滚动到底部（如用户发送消息），并恢复自动跟随
  const scrollToBottom = async () => {
    await nextTick();
    autoScroll.value = true;
    if (scrollRef.value) scrollRef.value.scrollTop = scrollRef.value.scrollHeight;
  };

  // 仅在自动跟随状态下滚动到底部（用于AI流式输出过程）
  const scrollToBottomIfAuto = async () => {
    await nextTick();
    if (autoScroll.value && !dragging && scrollRef.value) scrollRef.value.scrollTop = scrollRef.value.scrollHeight;
  };
  // update-end--author:liaozhiyang---date:20261027---for：【LHZP-1351】AI生成过程中用户向上滚动时不再强制滚动到底部

  const scrollToTop = async () => {
    await nextTick();
    if (scrollRef.value) scrollRef.value.scrollTop = 0;
  };

  const scrollToBottomIfAtBottom = async () => {
    await nextTick();
    if (scrollRef.value) {
      const threshold = 100; // Threshold, indicating the distance threshold to the bottom of the scroll bar.
      const distanceToBottom = scrollRef.value.scrollHeight - scrollRef.value.scrollTop - scrollRef.value.clientHeight;
      if (distanceToBottom <= threshold) scrollRef.value.scrollTop = scrollRef.value.scrollHeight;
    }
  };

  return {
    scrollRef,
    scrollToBottom,
    scrollToTop,
    scrollToBottomIfAtBottom,
    scrollToBottomIfAuto,
  };
}
