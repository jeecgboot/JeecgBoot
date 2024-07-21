/// <reference types="node" />
declare const ssrWindow: {
    document: {
        body: {};
        addEventListener(): void;
        removeEventListener(): void;
        activeElement: {
            blur(): void;
            nodeName: string;
        };
        querySelector(): any;
        querySelectorAll(): any[];
        getElementById(): any;
        createEvent(): {
            initEvent(): void;
        };
        createElement(): {
            children: any[];
            childNodes: any[];
            style: {};
            setAttribute(): void;
            getElementsByTagName(): any[];
        };
        createElementNS(): {};
        importNode(): any;
        location: {
            hash: string;
            host: string;
            hostname: string;
            href: string;
            origin: string;
            pathname: string;
            protocol: string;
            search: string;
        };
    };
    navigator: {
        userAgent: string;
    };
    location: {
        hash: string;
        host: string;
        hostname: string;
        href: string;
        origin: string;
        pathname: string;
        protocol: string;
        search: string;
    };
    history: {
        replaceState(): void;
        pushState(): void;
        go(): void;
        back(): void;
    };
    CustomEvent: () => any;
    addEventListener(): void;
    removeEventListener(): void;
    getComputedStyle(): {
        getPropertyValue(): string;
    };
    Image(): void;
    Date(): void;
    screen: {};
    setTimeout(): void;
    clearTimeout(): void;
    matchMedia(): {};
    requestAnimationFrame(callback: any): NodeJS.Timeout;
    cancelAnimationFrame(id: any): void;
};
declare function getWindow(): Window;
export { getWindow, ssrWindow };
