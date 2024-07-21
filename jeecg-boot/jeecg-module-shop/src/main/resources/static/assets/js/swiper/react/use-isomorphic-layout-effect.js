import { useEffect, useLayoutEffect } from 'react';

function useIsomorphicLayoutEffect(callback, deps) {
  // eslint-disable-next-line
  if (typeof window === 'undefined') return useEffect(callback, deps);
  return useLayoutEffect(callback, deps);
}

export { useIsomorphicLayoutEffect };