export class User {
    
    firstName: string;
    lastName: string;
    username: string;
    password:string;
    email: string;
    profileImageUrl: string;

    lastLoginDateDisplay: Date;
    createdDate: Date;
    lastModifiedDate: Date;
    
    active: boolean;
    accountNonLocked: boolean;
    NotExpired: boolean;
    credentialsNotExpired: boolean;
    suspended: boolean;
    banned: boolean;

    role: string;
    authorities: [];

    constructor();
    constructor(username:string, password:string);

    constructor(username?:string, password?:string) {
        this.firstName = '';
        this.lastName = '';
        this.username = username || '';
        this.password = password || '';
        this.email = '';
        this.active = false;
        this.accountNonLocked = false;
        this.NotExpired = false;
        this.credentialsNotExpired = false;
        this.suspended = false;
        this.banned = false;
        this.role = ''
        this.authorities = [];
    }
    
}