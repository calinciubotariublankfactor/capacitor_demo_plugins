import { WebPlugin } from '@capacitor/core';

import type { PaysafePluginPlugin } from './definitions';

export class PaysafePluginWeb extends WebPlugin implements PaysafePluginPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
