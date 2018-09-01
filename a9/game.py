import pygame
import time

from pygame.locals import*
from time import sleep

class Model():
    def __init__(self): #class constructor
            self.sprites = []
            self.mario = Mario(self)
            self.sprites.append(self.mario)

    def update(self):
            for sprite in self.sprites:
                sprite.update()
            for sprite in self.sprites:
                if (sprite.remove):
                    self.sprites.remove(sprite)

    def add_sprites(self, pos, type):
            x = pos[0] + self.mario.rect.left - self.mario.start
            if (type != "f"):
                for sprite in self.sprites:
                    if (sprite.clickSprite(x, pos[1])):
                        sprite.remove = True
                        return
            tmp = 0
            if (type == "t"):
                tmp = Tube(x, pos[1], self)
            if (type == "g"):
                tmp = Goomba(x, pos[1], self)
            if (type == "f"):
                tmp = Fireball(self)
            for s in self.sprites:
                if (tmp.checkOverlap(s)):
                    return
                
            self.sprites.append(tmp)
class Fireball():
    image = None
    type = "fireball"
    
    def __init__(self, model):
        self.model = model
        self.direction = self.model.mario.direction
        if self.image is None:
            self.image = pygame.image.load("fireball.png")
        self.rect = self.image.get_rect()
        self.setX()
        self.rect.top = self.model.mario.rect.top
        self.speed = 14
        self.vertV = 0
        self.prevX = 0
        self.prevY = 0
        self.remove = False
        self.burnTime = 0
        
    def draw(self, screen):
        screen.blit(self.image, ((self.rect.left - self.model.mario.rect.left + self.model.mario.start), self.rect.top))
    
    def update(self):
        self.prevX = self.rect.left
        self.prevY = self.rect.top
        
        self.vertV += 7
        self.rect.top += self.vertV
        
        if (self.rect.bottom > 400):
            self.vertV = -50
            self.rect.bottom = 400
            
        if (self.direction):
            self.rect.left += (self.speed)
        else:
            self.rect.left -= (self.speed)
            
        if (self.burnTime > 75): self.remove = True
        
        self.burnTime += 1
        
        self.collisonCorrection()
    
    def adjust(self, rect):
        if (self.prevX + (self.rect.right - self.rect.left) <= rect.left):
            self.rect.right = rect.left
            self.direction = not self.direction
            return
        elif (self.prevX >= rect.right):
            self.rect.left = rect.right
            self.direction = not self.direction
            return
        elif (self.prevY + (self.rect.bottom - self.rect.top) <= rect.top):
            self.vertV = 0
            self.rect.bottom = rect.top
            return
        elif (self.prevY >= rect.bottom):
            self.VertV = 0
            self.rect.top = rect.bottom
            return

    def clickSprite(self, x, y):
        if (x > self.rect.right):
            return False
        if (x < self.rect.left):
            return False
        if (y > self.rect.bottom):
            return False
        if (y < self.rect.top):
            return False
        
        return True
    
    def checkOverlap(self, s):
        if (self.rect.right < s.rect.left):
            return False
        if (self.rect.left > s.rect.right):
            return False
        if (self.rect.bottom < s.rect.top):
            return False
        if (self.rect.top > s.rect.bottom):
            return False
        
        return True
    
    def collisonCorrection(self):
            i = self.model.sprites.index(self)
            for j in xrange(len(self.model.sprites)):
                if (i != j):
                    temp = self.model.sprites[j]
                    if (self.checkOverlap(temp)):
                        if (self.type == "fireball"):
                            if (temp.type == "goomba"):
                                temp.onFire = True
                                self.remove = True
                                
                        self.adjust(temp.rect)
    def setX(self):
        if (self.direction):
            self.rect.left = self.model.mario.rect.right + 1
        else:
            self.rect.right = self.model.mario.rect.left - 1
class Goomba():
    image = None
    type = "goomba"
    def __init__(self, x, y, model):
        self.model = model
        self.direction = True
        if self.image is None:
            self.image = []
            self.image.append(pygame.image.load("goomba.png"))
            self.image.append(pygame.image.load("goomba_fire.png"))
        self.imageCount = 0
        self.rect = self.image[self.imageCount].get_rect()
        self.rect.left = x
        self.rect.top = y
        
        self.prevX = 0
        self.prevY = 0
        self.speed = 7
        self.vertV = -12
        self.remove = False
        self.onFire = False
        self.burnTime = 0
        
    def draw(self, screen):
        screen.blit(self.image[self.imageCount], ((self.rect.left - self.model.mario.rect.left + self.model.mario.start), self.rect.top))
        
    def update(self):
        self.prevX = self.rect.left
        self.prevY = self.rect.top
        
        self.vertV += 7
        self.rect.top += self.vertV
        
        if (self.rect.bottom > 400):
            self.vertV = 0
            self.rect.bottom = 400
        
        if(self.onFire):
            self.burnTime += 1
            if (self.direction):
                self.rect.left += ((1.5) * self.speed)
            else:
                self.rect.left -= ((1.5) * self.speed)
            
            self.imageCount = 1
            
            if (self.burnTime > 40):
                self.remove = True
        else:
            if (self.direction):
                self.rect.left += (self.speed)
            else:
                self.rect.left -= (self.speed)
        
        self.collisonCorrection()
        
    def adjust(self, rect):
        if (self.prevX + (self.rect.right - self.rect.left) <= rect.left):
            self.rect.right = rect.left
            self.direction = not self.direction
            return
        elif (self.prevX >= rect.right):
            self.rect.left = rect.right
            self.direction = not self.direction
            return
        elif (self.prevY + (self.rect.bottom - self.rect.top) <= rect.top):
            self.vertV = 0
            self.rect.bottom = rect.top
            return
        elif (self.prevY >= rect.bottom):
            self.VertV = 0
            self.rect.top = rect.bottom
            return

    def clickSprite(self, x, y):
        if (x > self.rect.right):
            return False
        if (x < self.rect.left):
            return False
        if (y > self.rect.bottom):
            return False
        if (y < self.rect.top):
            return False
        
        return True
    
    def checkOverlap(self, s):
        if (self.rect.right < s.rect.left):
            return False
        if (self.rect.left > s.rect.right):
            return False
        if (self.rect.bottom < s.rect.top):
            return False
        if (self.rect.top > s.rect.bottom):
            return False
        
        return True
    
    def collisonCorrection(self):
            i = self.model.sprites.index(self)
            for j in xrange(len(self.model.sprites)):
                if (i != j):
                    temp = self.model.sprites[j]
                    if (self.checkOverlap(temp)):
                        if (self.type == "fireball"):
                            if (temp.type == "goomba"):
                                temp.onFire = True
                                self.remove = True
                        self.adjust(temp.rect)
class Mario():
    image = None
    type = "mario"
    def __init__(self, model):
        self.model = model
        self.direction = True
        self.start = 200
        if self.image is None:
            self.image = []
            self.image.append(pygame.image.load("mario1.png"))
            self.image.append(pygame.image.load("mario2.png"))
            self.image.append(pygame.image.load("mario3.png"))
            self.image.append(pygame.image.load("mario4.png"))
            self.image.append(pygame.image.load("mario5.png"))
        
        self.imageCount = 0
        self.rect = self.image[self.imageCount].get_rect()
        self.rect.left = self.start
        self.rect.top = 200
        
        self.jumpCount = 0
        self.moveLeft = False
        self.moveRight = False
        self.moveJump = False
        
        self.prevX = 0
        self.prevY = 0
        self.speed = 10
        self.vertV = -12
        self.remove = False
        
    def draw(self, screen):
        screen.blit(self.image[self.imageCount], (self.start, self.rect.top))
    def update(self):
        self.prevX = self.rect.left
        self.prevY = self.rect.top
        
        if (self.moveLeft):
            self.rect.left -= self.speed
        if (self.moveRight):
            self.rect.left += self.speed
        if (self.moveJump):
            if (self.jumpCount < 5):
                self.vertV -= 17
            self.jumpCount += 1
            
        self.vertV += 7
        self.rect.top += self.vertV
        
        if (self.rect.bottom > 400):
            self.vertV = 0
            self.rect.bottom = 400
            self.jumpCount = 0
        
        if (self.moveLeft or self.moveRight):
            self.imageCount += 1
            if (self.imageCount > 4):
                self.imageCount = 0
        else:
            self.imageCount = 3
        
        self.moveLeft = False
        self.moveRight = False
        self.moveJump = False
        
        self.collisonCorrection()
        
    def collisonCorrection(self):
            i = self.model.sprites.index(self)
            for j in xrange(len(self.model.sprites)):
                if (i != j):
                    temp = self.model.sprites[j]
                    if (self.checkOverlap(temp)):
                        if (self.type == "fireball"):
                            if (temp.type == "goomba"):
                                temp.onFire = True
                                self.remove = True
                        self.adjust(temp.rect)    
    def clickSprite(self, x, y):
        return False
    
    def adjust(self, rect):
        if (self.prevX + (self.rect.right - self.rect.left) <= rect.left):
            self.rect.right = rect.left
            return
        elif (self.prevX >= rect.right):
            self.rect.left = rect.right
            return
        elif (self.prevY + (self.rect.bottom - self.rect.top) <= rect.top):
            self.vertV = 0
            self.rect.bottom = rect.top
            self.jumpCount = 0
            return
        elif (self.prevY >= rect.bottom):
            self.VertV = 0
            self.rect.top = rect.bottom
            return

    def checkOverlap(self, s):
        if (self.rect.right < s.rect.left):
            return False
        if (self.rect.left > s.rect.right):
            return False
        if (self.rect.bottom < s.rect.top):
            return False
        if (self.rect.top > s.rect.bottom):
            return False
        
        return True
    
class Tube():
    image = None
    type = "tube"
    def __init__(self, x, y, model):
            self.model = model
            if self.image is None: #lazy loading using class variable
                self.image = pygame.image.load("tube.png")
            self.rect = self.image.get_rect()
            self.rect.left = x
            self.rect.top = y
            self.remove = False
    def update(self):
            pass
    def checkOverlap(self, s):
        if (self.rect.right < s.rect.left):
            return False
        if (self.rect.left > s.rect.right):
            return False
        if (self.rect.bottom < s.rect.top):
            return False
        if (self.rect.top > s.rect.bottom):
            return False
        
        return True
    def adjust(self, rect):
        pass
    def clickSprite(self, x, y):
        if (x > self.rect.right):
            return False
        if (x < self.rect.left):
            return False
        if (y > self.rect.bottom):
            return False
        if (y < self.rect.top):
            return False
        
        return True
    
    def draw(self, screen):
            screen.blit(self.image, ((self.rect.left - self.model.mario.rect.left + self.model.mario.start), self.rect.top))
    
class View():
    def __init__(self, model): #class constructor
        screen_size = (800,600) 
        self.screen = pygame.display.set_mode(screen_size, 32)
        self.model = model

    def update(self):    
            self.screen.fill([40,100, 200])
            self.screen.fill([0,200,100], (0, 400, 1000, 1000))
            for s in self.model.sprites:
                s.draw(self.screen)
            pygame.display.flip()

class Controller():
    def __init__(self, model):
        self.model = model
        self.keep_going = True

    def update(self):
        for event in pygame.event.get():
            if event.type == QUIT:
                self.keep_going = False
            elif event.type == KEYDOWN:
                if event.key == K_ESCAPE:
                    self.keep_going = False
            elif event.type == pygame.MOUSEBUTTONUP:
                if (event.button == 1):
                    self.model.add_sprites(pygame.mouse.get_pos(), "t")
                if (event.button == 3):
                    self.model.add_sprites(pygame.mouse.get_pos(), "g")
        keys = pygame.key.get_pressed()
        if keys[K_LCTRL]:
            self.model.add_sprites(pygame.mouse.get_pos(), "f")
        if keys[K_LEFT]:
            self.model.mario.moveLeft = True
        if keys[K_RIGHT]:
            self.model.mario.moveRight = True
        if keys[K_SPACE]:
            self.model.mario.moveJump = True

# "game" section
print("Please add Tubes with left click, Goombas with right click, and fireballs with Left Control. Move Mario as expected. Press Esc to quit.")
pygame.init()
m = Model()
v = View(m)
c = Controller(m)
while c.keep_going:
    c.update()
    m.update()
    v.update()
    sleep(0.04) #determines frame rate
print("Goodbye")
