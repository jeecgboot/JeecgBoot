export const localData = {
    active: 1002,
    usingContext: true,
    history: [
      {
        title: '标题02',
        uuid: 1706083575869,
        isEdit: false,
      },
      {
        uuid: 1002,
        title: '标题01',
        isEdit: false,
      },
    ],
    chat: [
      {
        uuid: 1706083575869,
        data: [
          {
            dateTime: '2024/1/24 16:06:27',
            text: '？',
            inversion: true,
            error: false,
            conversationOptions: null,
            requestOptions: {
              prompt: '？',
              options: null,
            },
          },
          {
            dateTime: '2024/1/24 16:06:29',
            text: 'Hello! How can I assist you today?',
            inversion: false,
            error: false,
            loading: false,
            conversationOptions: {
              parentMessageId: 'chatcmpl-8kSZA0wju7X8sOdJIyxtpDj0RQVu1',
            },
            requestOptions: {
              prompt: '？',
              options: {},
            },
          },
        ],
      },
      {
        uuid: 1002,
        data: [
          {
            dateTime: '2024/1/24 14:01:52',
            text: '1',
            inversion: true,
            error: false,
            conversationOptions: null,
            requestOptions: {
              prompt: '1',
              options: null,
            },
          },
          {
            dateTime: '2024/1/24 14:01:54',
            text: 'Yes, how can I assist you?',
            inversion: false,
            error: false,
            loading: false,
            conversationOptions: {
              parentMessageId: 'chatcmpl-8kQcb6mbF04o5hpule4SdHk2jFvNQ',
            },
            requestOptions: {
              prompt: '1',
              options: {},
            },
          },
          {
            dateTime: '2024/1/24 14:03:45',
            text: '？',
            inversion: true,
            error: false,
            conversationOptions: null,
            requestOptions: {
              prompt: '？',
              options: null,
            },
          },
          {
            dateTime: '2024/1/24 14:03:47',
            text: "I'm sorry if my previous response was not clear. Please let me know how I can help you or what you would like to discuss.",
            inversion: false,
            error: false,
            loading: false,
            conversationOptions: {
              parentMessageId: 'chatcmpl-8kQeQ2t8YCXmLeF0ECGkkuOJlk4Pi',
            },
            requestOptions: {
              prompt: '？',
              options: {
                parentMessageId: 'chatcmpl-8kQcb6mbF04o5hpule4SdHk2jFvNQ',
              },
            },
          },
          {
            dateTime: '2024/1/24 14:10:19',
            text: 'js   递归',
            inversion: true,
            error: false,
            conversationOptions: null,
            requestOptions: {
              prompt: 'js   递归',
              options: null,
            },
          },
          {
            dateTime: '2024/1/24 14:10:33',
            text: 'JavaScript supports recursion, which is the process of a function calling itself. Recursion can be useful for solving problems that can be broken down into smaller, similar sub-problems.\n\nHere\'s an example of a simple recursive function in JavaScript:\n\n```javascript\nfunction countdown(n) {\n  if (n <= 0) {\n    console.log("Done!");\n  } else {\n    console.log(n);\n    countdown(n - 1); // recursive call\n  }\n}\n\ncountdown(5);\n```\n\nIn this example, the `countdown` function takes an argument `n` and logs the value of `n` to the console. If `n` is greater than zero, it then calls itself with `n - 1`. This process continues until `n` becomes less than or equal to zero, at which point it logs "Done!".\n\nRecursion can be helpful in solving problems that involve tree structures, factorial calculations, searching algorithms, and more. However, it\'s important to use recursion properly to avoid infinite loops or excessive stack usage.',
            inversion: false,
            error: false,
            loading: false,
            conversationOptions: {
              parentMessageId: 'chatcmpl-8kQkmCbnRe4fG1FhWTlY0EyHTpqau',
            },
            requestOptions: {
              prompt: 'js   递归',
              options: {
                parentMessageId: 'chatcmpl-8kQeQ2t8YCXmLeF0ECGkkuOJlk4Pi',
              },
            },
          },
          {
            dateTime: '2024/1/24 14:17:15',
            text: 'js  递归',
            inversion: true,
            error: false,
            conversationOptions: null,
            requestOptions: {
              prompt: 'js  递归',
              options: null,
            },
          },
          {
            dateTime: '2024/1/24 14:23:50',
            text: "Certainly! Here's an example of how you can use recursion in JavaScript:\n\n```javascript\nfunction factorial(n) {\n  if (n === 0) {\n    return 1;\n  } else {\n    return n * factorial(n - 1);\n  }\n}\n\nconsole.log(factorial(5)); // Output: 120\n```\n\nIn this example, the `factorial` function calculates the factorial of a given number `n` using recursion. If `n` is equal to 0, it returns 1, which is the base case. Otherwise, it recursively calls itself with `n - 1`, multiplying the current value of `n` with the result of the recursive call.\n\nWhen calling `factorial(5)`, the function will execute as follows:\n\n- `factorial(5)` calls `factorial(4)`\n- `factorial(4)` calls `factorial(3)`\n- `factorial(3)` calls `factorial(2)`\n- `factorial(2)` calls `factorial(1)`\n- `factorial(1)` calls `factorial(0)`\n- `factorial(0)` returns 1\n- `factorial(1)` returns 1 * 1 = 1\n- `factorial(2)` returns 2 * 1 = 2\n- `factorial(3)` returns 3 * 2 = 6\n- `factorial(4)` returns 4 * 6 = 24\n- `factorial(5)` returns 5 * 24 = 120\n\nThe final result is then printed to the console using `console.log`.",
            inversion: false,
            error: false,
            loading: false,
            conversationOptions: {
              parentMessageId: 'chatcmpl-8kQwWVoZoWyqjbWuwMJmu6w3hBvXj',
            },
            requestOptions: {
              prompt: 'js  递归',
              options: {
                parentMessageId: 'chatcmpl-8kQkmCbnRe4fG1FhWTlY0EyHTpqau',
              },
            },
          },
          {
            dateTime: '2024/1/24 15:05:30',
            text: '///',
            inversion: true,
            error: false,
            conversationOptions: null,
            requestOptions: {
              prompt: '///',
              options: null,
            },
          },
          {
            dateTime: '2024/1/24 15:05:33',
            text: "I apologize if my previous response was not what you were expecting. If you have any specific questions or need further assistance, please let me know and I'll be happy to help.",
            inversion: false,
            error: false,
            loading: false,
            conversationOptions: {
              parentMessageId: 'chatcmpl-8kRcAggkC4u47d34UcQW3cI0htw0w',
            },
            requestOptions: {
              prompt: '///',
              options: {
                parentMessageId: 'chatcmpl-8kQwWVoZoWyqjbWuwMJmu6w3hBvXj',
              },
            },
          },
        ],
      },
    ],
  };
