// Powered by OnSpace.AI
import { MANUFACTURERS, MODELS } from '@/constants/config';

function randInt(min: number, max: number): number {
  return Math.floor(Math.random() * (max - min + 1)) + min;
}

function randHex(len: number): string {
  return Array.from({ length: len }, () =>
    randInt(0, 15).toString(16)
  ).join('');
}

export function randomIPv4(): string {
  return `${randInt(10, 192)}.${randInt(0, 255)}.${randInt(0, 255)}.${randInt(1, 254)}`;
}

export function randomIPv6(): string {
  const groups = Array.from({ length: 8 }, () => randHex(4));
  return groups.join(':');
}

export function randomAndroidId(): string {
  return randHex(16);
}

export function randomManufacturer(): string {
  return MANUFACTURERS[randInt(0, MANUFACTURERS.length - 1)];
}

export function randomModel(manufacturer: string): string {
  const list = MODELS[manufacturer] || MODELS['Google'];
  return list[randInt(0, list.length - 1)];
}

const BUILD_TAGS = ['user/release-keys', 'userdebug/test-keys', 'user/dev-keys'];
export function randomFingerprint(manufacturer: string, model: string, buildId: string): string {
  const brand  = manufacturer.toLowerCase();
  const device = model.replace(/[^a-zA-Z0-9]/g, '').toLowerCase().slice(0, 8);
  const tag    = BUILD_TAGS[randInt(0, BUILD_TAGS.length - 1)];
  const sdk    = randInt(29, 34);
  return `${brand}/${device}/${device}:${sdk === 29 ? 10 : sdk === 30 ? 11 : sdk === 31 ? 12 : sdk === 32 ? 12 : sdk === 33 ? 13 : 14}/${buildId}/${randInt(8000000, 9999999)}:${tag}`;
}

export function randomBuildId(): string {
  const letters = ['SQ', 'TP', 'UP', 'TQ', 'SP'];
  const letter  = letters[randInt(0, letters.length - 1)];
  return `${letter}${randInt(1, 9)}A.${randInt(200000, 230000)}.${randInt(1, 99).toString().padStart(3, '0')}`;
}

export function generateFullRandom() {
  const manufacturer = randomManufacturer();
  const model        = randomModel(manufacturer);
  const buildId      = randomBuildId();
  const fingerprint  = randomFingerprint(manufacturer, model, buildId);
  return {
    ipv4:         randomIPv4(),
    ipv6:         randomIPv6(),
    manufacturer,
    model,
    androidId:    randomAndroidId(),
    fingerprint,
    brand:        manufacturer.toLowerCase(),
    device:       model.replace(/[^a-zA-Z0-9]/g, '').toLowerCase().slice(0, 8),
    product:      model.replace(/[^a-zA-Z0-9]/g, '').toLowerCase().slice(0, 8),
    buildId,
    sdkInt:       String(randInt(29, 34)),
  };
}
