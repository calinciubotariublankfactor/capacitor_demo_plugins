import { WebPlugin } from '@capacitor/core';

import type { PaysafePluginPlugin } from './definitions';

export class PaysafePluginWeb extends WebPlugin implements PaysafePluginPlugin {
  async echo(): Promise<void> {
    console.log('ECHO');
  }

  async startVenmo(): Promise<string> {
    console.log('ECHO');
    return Promise.resolve("echo")
  }
}
