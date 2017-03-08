export class Consultant {
    dn: string;
    cn: string;
    firstName: string;
    lastName: string;
    organization: string;
    mobile: string;
    mail: string;
    owner: string;
    inviteTimeStamp: string;
    state: string;
    expireDate: any;

    setExpireDate(date: any) {
        if (date && date.jsdate) {
            this.expireDate = date.jsdate;
        } else if (date) {
            this.expireDate = date;
        }
    }
}
