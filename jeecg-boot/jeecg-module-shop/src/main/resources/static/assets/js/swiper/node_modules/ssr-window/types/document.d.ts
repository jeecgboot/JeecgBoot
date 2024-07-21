declare const ssrDocument: {
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
declare function getDocument(): Document;
export { getDocument, ssrDocument };
