import { darkCssIsReady, loadDarkThemeCss } from '@rys-fe/vite-plugin-theme/es/client';
import { addClass, hasClass, removeClass } from '/@/utils/domUtils';

export async function updateDarkTheme(mode: string | null = 'light') {
  const htmlRoot = document.getElementById('htmlRoot');
  if (!htmlRoot) {
    return;
  }
  const hasDarkClass = hasClass(htmlRoot, 'dark');
  if (mode === 'dark') {
    if (import.meta.env.PROD && !darkCssIsReady) {
      await loadDarkThemeCss();
    }
    htmlRoot.setAttribute('data-theme', 'dark');
    if (!hasDarkClass) {
      addClass(htmlRoot, 'dark');
    }
  } else {
    htmlRoot.setAttribute('data-theme', 'light');
    if (hasDarkClass) {
      removeClass(htmlRoot, 'dark');
    }
  }
}
