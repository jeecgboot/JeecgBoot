import { onMounted, onUnmounted, ref } from 'vue';

interface ScriptOptions {
  src: string;
}

export function useScript(opts: ScriptOptions) {
  const isLoading = ref(false);
  const error = ref(false);
  const success = ref(false);
  let script: HTMLScriptElement;

  const promise = new Promise((resolve, reject) => {
    onMounted(() => {
      script = document.createElement('script');
      script.type = 'text/javascript';
      script.onload = function () {
        isLoading.value = false;
        success.value = true;
        error.value = false;
        resolve('');
      };

      script.onerror = function (err) {
        isLoading.value = false;
        success.value = false;
        error.value = true;
        reject(err);
      };

      script.src = opts.src;
      document.head.appendChild(script);
    });
  });

  onUnmounted(() => {
    script && script.remove();
  });

  return {
    isLoading,
    error,
    success,
    toPromise: () => promise,
  };
}
