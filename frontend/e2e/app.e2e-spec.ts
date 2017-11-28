import { AngularGpsPage } from './app.po';

describe('angular-gps App', function() {
  let page: AngularGpsPage;

  beforeEach(() => {
    page = new AngularGpsPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
