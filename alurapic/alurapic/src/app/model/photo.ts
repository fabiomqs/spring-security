export interface Photo {
    id: number;
    postDate: Date;
    url: string;
    description: string;
    allowComments: boolean;
    likes:number;
    numberOfcomments:number;
    username: string;
}