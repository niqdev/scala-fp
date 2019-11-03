// See https://docusaurus.io/docs/site-config

const repoUrl = 'https://github.com/niqdev/scala-fp';

const siteConfig = {
  title: 'Scala FP for dummies',
  tagline: 'A website for testing',
  url: 'https://niqdev.github.io',
  baseUrl: '/scala-fp/',

  // mdoc configuration
  customDocsPath: 'modules/docs/target/mdoc',

  // Used for publishing and more
  projectName: 'scala-fp',
  organizationName: 'niqdev',

  // For no header links in the top nav bar -> headerLinks: [],
  headerLinks: [
    { doc: 'basic', label: 'Basic'},
    { doc: 'fp', label: 'FP'},
    { doc: 'ecosystem', label: 'Ecosystem'},
    { doc: 'old', label: 'Old'},
    { href: repoUrl, label: "GitHub" },
  ],

  /* path to images for header/footer */
  headerIcon: 'img/favicon.ico',
  footerIcon: 'img/favicon.ico',
  favicon: 'img/favicon.ico',

  /* Colors for website */
  colors: {
    primaryColor: "#15414c",
    secondaryColor: "#244e58"
  },

  /* Custom fonts for website */
  /*
  fonts: {
    myFont: [
      "Times New Roman",
      "Serif"
    ],
    myOtherFont: [
      "-apple-system",
      "system-ui"
    ]
  },
  */

  // This copyright info is used in /core/Footer.js and blog RSS/Atom feeds.
  copyright: `Copyright © ${new Date().getFullYear()} niqdev`,

  highlight: {
    // Highlight.js theme to use for syntax highlighting in code blocks.
    theme: 'github',
  },

  // On page navigation for the current documentation page.
  onPageNav: 'separate',
  // No .html extensions for paths.
  cleanUrl: true,

  // For sites with a sizable amount of content, set collapsible to true.
  // Expand/collapse the links and subcategories under categories.
  // docsSideNavCollapsible: true,

  // Show documentation's last contributor's name.
  // enableUpdateBy: true,

  // Show documentation's last update time.
  // enableUpdateTime: true,

  // You may provide arbitrary config keys to be used as needed by your
  // template. For example, if you need your repo's URL...
  repoUrl,
};

module.exports = siteConfig;
