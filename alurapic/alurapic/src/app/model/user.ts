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
    constructor(firstName: string, lastName: string, email: string, username:string, role: string);

    constructor(firstName?: string, lastName?: string, email?: string, username?:string, password?:string, role?: string) {
        this.firstName = firstName || '';
        this.lastName = lastName || '';
        this.username = username || '';
        this.password = password || '';
        this.email = email || '';
        this.active = false;
        this.accountNonLocked = false;
        this.NotExpired = false;
        this.credentialsNotExpired = false;
        this.suspended = false;
        this.banned = false;
        this.role = role || ''
        this.authorities = [];
    }
    
}