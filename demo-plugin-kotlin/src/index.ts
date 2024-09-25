import { registerPlugin } from '@capacitor/core';

import type { PaysafePluginPlugin } from './definitions';

const PaysafePlugin = registerPlugin<PaysafePluginPlugin>('PaysafePlugin', {
  web: () => import('./web').then(m => new m.PaysafePluginWeb()),
});

export * from './definitions';
export { PaysafePlugin };
