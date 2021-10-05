export class ServerLog {
    type: string;
    message: string;
    url: string;
    username: string;
    stack: string;

    constructor(type: string, message: string, url: string, username: string, stack: string) {
        this.type = type;
        this.message = message;
        this.url = url;
        this.username = username;
        this.stack = stack;
    }
}