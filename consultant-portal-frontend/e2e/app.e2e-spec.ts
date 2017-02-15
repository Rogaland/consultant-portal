import { ConsultantPortalFrontendPage } from './app.po';

describe('consultant-portal-frontend App', function() {
  let page: ConsultantPortalFrontendPage;

  beforeEach(() => {
    page = new ConsultantPortalFrontendPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
