import random
import pygame
import os
import math
pygame.init()


WIN_WIDTH, WIN_HEIGHT = (1000, 700)
GAP = 10
WHITE = (255, 255, 255)
BLACK = (0, 0, 0)
GRAY = (90, 90, 90)
DARK_GRAY = (60, 60, 60)
BLUE = (78, 178, 210)
PURPLE = (201, 155, 203)
GREEN = (0, 180, 150)

FONT = pygame.font.SysFont("freesans", 20)
SMALL_FONT = pygame.font.SysFont("freesans", 15)

os.environ['SDL_VIDEO_WINDOW_POS'] = "%d,%d" %(300, 100)
win = pygame.display.set_mode((WIN_WIDTH, WIN_HEIGHT))
pygame.display.set_caption('Sorting Algorithms Visualizer')

def gen_array(nums, num_nums, MIN_RANGE, MAX_RANGE):
	nums.clear()
	for x in range(num_nums):
		x = random.randint(MIN_RANGE, MAX_RANGE)
		nums.append(x)

def text_objects(text, font):
	textsurf = FONT.render(text, True, WHITE)
	return textsurf, textsurf.get_rect()

class Button():
	def __init__(self, num):
		self.WIDTH, self.HEIGHT = (100, 30)
		self.num = num
		self.x = GAP + (self.num)*self.WIDTH + (num)*GAP + 110
		self.y = 50
		self.pressed_down = False
		self.button = pygame.Rect(self.x, self.y, self.WIDTH, self.HEIGHT)

	def draw(self, win):
		pygame.draw.rect(win, GRAY, self.button)

	def hover(self, win):
		x, y = pygame.mouse.get_pos()
		if x >= self.x and x <= self.x + self.WIDTH and y >= self.y and y <= self.y + self.HEIGHT:
			pygame.draw.rect(win, DARK_GRAY, self.button)
			return True

	def text(self, win):
		if self.num == 0:
			text = "Bubble sort"
		elif self.num == 1:
			text = "Selection sort"
		elif self.num == 2:
			text = "Insertion sort"
		elif self.num == 3:
			text = "Heap sort"
		elif self.num == 4:
			text = "Merge sort"
		elif self.num == 5:
			text = "Quick sort"
		elif self.num == 6:
			text = "New array"
		elif self.num == 7:
			text = "Visualize"
		textSurf, textRect = text_objects(text, FONT)
		textRect.center = ((self.x + self.WIDTH/2), (self.y + self.HEIGHT/2))
		win.blit(textSurf, textRect)

	def pressed(self, win):
		click = pygame.mouse.get_pressed()
		if click[0] == 1 and self.hover(win):
			self.pressed_down = True
		
		if self.pressed_down:
			pygame.draw.rect(win, GREEN, self.button)

	def unpressed(self, num):
		if self.num == num:
			self.pressed_down = False

def execute(execute_list, nums, bars, buttons, win, num_nums, slider, MIN_RANGE, MAX_RANGE):
	for button in buttons:
		if execute_list[0] and execute_list[7]:
			bubble_sort(nums, bars, buttons, win, execute_list, num_nums, slider, MIN_RANGE, MAX_RANGE)
			button.unpressed(7)
			button.unpressed(0)
		if execute_list[1] and execute_list[7]:
			selection_sort(nums, bars, buttons, win, execute_list, num_nums, slider, MIN_RANGE, MAX_RANGE)
			button.unpressed(7)
			button.unpressed(1)
		if execute_list[2] and execute_list[7]:
			insertion_sort(nums, bars, buttons, win, execute_list, num_nums, slider, MIN_RANGE, MAX_RANGE)
			button.unpressed(7)
			button.unpressed(2)
		if execute_list[3] and execute_list[7]:
			heap_sort(nums, bars, buttons, win, execute_list, num_nums, slider, MIN_RANGE, MAX_RANGE)
			button.unpressed(7)
			button.unpressed(3)
		if execute_list[4] and execute_list[7]:
			merge_sort(nums, bars, buttons, win, execute_list, num_nums, slider, MIN_RANGE, MAX_RANGE)
			button.unpressed(7)
			button.unpressed(4)
		if execute_list[5] and execute_list[7]:
			quick_sort(nums, bars, buttons, win, execute_list, num_nums, slider, MIN_RANGE, MAX_RANGE)
			button.unpressed(7)
			button.unpressed(5)
		if execute_list[6]:
			redraw_bars(bars, win, num_nums, nums)
			button.unpressed(6)

class Slider():
	def __init__(self, num_nums):
		self.MIN_X = 11
		self.MAX_X = 110
		self.circle_center = 10 + num_nums
		self.pressed_down = False

	def hover(self, win):
		x, y = pygame.mouse.get_pos()
		dist = math.sqrt((x - self.circle_center)**2 + (y - 65)**2)
		if dist < 7:
			return True
			if self.pressed_down:
				return False

	def draw(self, bars, win, num_nums):
		pygame.draw.line(win, GRAY, (self.MIN_X, 65), (self.MAX_X, 65))

		if self.pressed_down:
			pygame.draw.circle(win, GREEN, (self.circle_center, 65), 7)
		elif self.hover(win):
			pygame.draw.circle(win, DARK_GRAY, (self.circle_center, 65), 7)
		else:
			pygame.draw.circle(win, GRAY, (self.circle_center, 65), 7)
		
		textSurf = SMALL_FONT.render("Array size: " + str(num_nums), True, BLACK)
		win.blit(textSurf, (25, 25))

	def pressed(self, win):
		x, y = pygame.mouse.get_pos()
		click = pygame.mouse.get_pressed()
		if click[0] == 1 and self.hover(win):
			self.pressed_down = True
		
		if click[0] == 0:
			self.pressed_down = False

		if self.pressed_down:
			self.circle_center = x
			if self.circle_center > self.MAX_X:
				self.circle_center = self.MAX_X
			elif self.circle_center < self.MIN_X:
				self.circle_center = self.MIN_X

def change_num_nums(slider):
	return slider.circle_center - 10

class Bar():
	def __init__(self, num, value, num_nums, MIN_RANGE, MAX_RANGE):
		MIN = 100
		MAX = WIN_HEIGHT
		MAX_WIDTH = 100
		MIN_WIDTH = 5
		BAR_GAP = 3
		bar_width = int((500)/num_nums)
		self.y = MIN
		if bar_width > MAX_WIDTH:
			bar_width = MAX_WIDTH
		elif bar_width < MIN_WIDTH:
			bar_width = MIN_WIDTH
		
		bar_height = value/(MAX_RANGE - (MIN_RANGE - 1))*(MAX - MIN - 10) 
		gap = (800 - (num_nums*bar_width + (num_nums - 1)*BAR_GAP))/2

		self.x = 100 + (num)*(bar_width + BAR_GAP) + gap
		self.bar = pygame.Rect(self.x, self.y, bar_width, bar_height)
	
	def draw(self, win):
		pygame.draw.rect(win, BLUE, self.bar)
		pygame.draw.line(win, BLACK, (100, self.y), (900, self.y), 1)

def redraw_bars(bars, win, num_nums, nums):
	bars.clear()
	MIN_RANGE = 1
	MAX_RANGE = 1000
	for i in range(num_nums):
		bar = Bar(i, nums[i], num_nums, MIN_RANGE, MAX_RANGE)
		bars.append(bar)

	for bar in bars:
		bar.draw(win)

def draw_window(win, buttons, bars, execute_list, slider, num_nums, nums, MIN_RANGE, MAX_RANGE):
	win.fill(WHITE)

	for button in buttons:
		button.draw(win)
		button.hover(win)
		button.pressed(win)
		button.text(win)

		if button.num == 0 and button.pressed_down:
			execute_list[0] = True
			for button in buttons:
				if button.num != 0: 
					if button.num == 7:
						pass
					else:	
						button.unpressed(button.num)
						execute_list[button.num] = False
		if button.num == 1 and button.pressed_down:
			execute_list[1] = True
			for button in buttons:
				if button.num != 1:
					if button.num == 7:
						pass
					else:	
						button.unpressed(button.num)
						execute_list[button.num] = False
		if button.num == 2 and button.pressed_down:
			execute_list[2] = True
			for button in buttons:
				if button.num != 2:
					if button.num == 7:
						pass
					else:	
						button.unpressed(button.num)
						execute_list[button.num] = False
		if button.num == 3 and button.pressed_down:
			execute_list[3] = True
			for button in buttons:
				if button.num != 3:
					if button.num == 7:
						pass
					else:	
						button.unpressed(button.num)
						execute_list[button.num] = False
		if button.num == 4 and button.pressed_down:
			execute_list[4] = True
			for button in buttons:
				if button.num != 4:
					if button.num == 7:
						pass
					else:	
						button.unpressed(button.num)
						execute_list[button.num] = False
		if button.num == 5 and button.pressed_down:
			execute_list[5] = True
			for button in buttons:
				if button.num != 5:
					if button.num == 7:
						pass
					else:	
						button.unpressed(button.num)
						execute_list[button.num] = False
		if button.num == 6 and button.pressed_down:
			execute_list[6] = True
			gen_array(nums, num_nums, MIN_RANGE, MAX_RANGE)
			button.unpressed(6)

		if button.num == 7 and button.pressed_down:
			execute_list[7] = True
	
	for bar in bars:
		bar.draw(win)
	
	slider.draw(bars, win, num_nums)
	slider.hover(win)
	slider.pressed(win)

	pygame.display.update()

def bubble_sort(nums, bars, buttons, win, execute_list, num_nums, slider, MIN_RANGE, MAX_RANGE):
	swapped = True
	while swapped:
		swapped = False
		for i in range(len(nums) - 1):
			if nums[i] > nums[i + 1]:
				nums[i], nums[i + 1] = nums[i + 1], nums[i]
				redraw_bars(bars, win, num_nums, nums)
				draw_window(win, buttons, bars, execute_list, slider, num_nums, nums, MIN_RANGE, MAX_RANGE)
				swapped = True

def selection_sort(nums, bars, buttons, win, execute_list, num_nums, slider, MIN_RANGE, MAX_RANGE):
	for i in range(len(nums)):
		lowest_value = i
		for j in range(i + 1, len(nums)):
			if nums[j] < nums[lowest_value]:
				lowest_value = j
			redraw_bars(bars, win, num_nums, nums)
			draw_window(win, buttons, bars, execute_list, slider, num_nums, nums, MIN_RANGE, MAX_RANGE)
		nums[i], nums[lowest_value] = nums[lowest_value], nums[i]

def insertion_sort(nums, bars, buttons, win, execute_list, num_nums, slider, MIN_RANGE, MAX_RANGE):
	for i in range(1, len(nums)):
		item_to_insert = nums[i]
		j = i - 1
		while j >= 0 and nums[j] > item_to_insert:
			nums[j + 1] = nums[j]
			j -= 1
			redraw_bars(bars, win, num_nums, nums)
			draw_window(win, buttons, bars, execute_list, slider, num_nums, nums, MIN_RANGE, MAX_RANGE)
		nums[j + 1] = item_to_insert

def heapify(nums, heap_size, root_index, bars, buttons, win, execute_list, num_nums, slider, MIN_RANGE, MAX_RANGE):
	largest_index = root_index
	left_child_index = (2 * root_index) + 1
	right_child_index = (2 * root_index) + 2

	if left_child_index < heap_size and nums[left_child_index] > nums[largest_index]:
		largest_index = left_child_index
		redraw_bars(bars, win, num_nums, nums)
		draw_window(win, buttons, bars, execute_list, slider, num_nums, nums, MIN_RANGE, MAX_RANGE)

	if right_child_index < heap_size and nums[right_child_index] > nums[largest_index]:
		largest_index = right_child_index
		redraw_bars(bars, win, num_nums, nums)
		draw_window(win, buttons, bars, execute_list, slider, num_nums, nums, MIN_RANGE, MAX_RANGE)

	if largest_index != root_index:
		nums[root_index], nums[largest_index] = nums[largest_index], nums[root_index]
		redraw_bars(bars, win, num_nums, nums)
		draw_window(win, buttons, bars, execute_list, slider, num_nums, nums, MIN_RANGE, MAX_RANGE)
		heapify(nums, heap_size, largest_index, bars, buttons, win, execute_list, num_nums, slider, MIN_RANGE, MAX_RANGE) 

def heap_sort(nums, bars, buttons, win, execute_list, num_nums, slider, MIN_RANGE, MAX_RANGE):
	n = len(nums)
	for i in range(n, -1, -1):
		heapify(nums, n, i, bars, buttons, win, execute_list, num_nums, slider, MIN_RANGE, MAX_RANGE)

	for i in range(n - 1, 0, -1):
		nums[i], nums[0] = nums[0], nums[i]
		redraw_bars(bars, win, num_nums, nums)
		draw_window(win, buttons, bars, execute_list, slider, num_nums, nums, MIN_RANGE, MAX_RANGE)
		heapify(nums, i, 0, bars, buttons, win, execute_list, num_nums, slider, MIN_RANGE, MAX_RANGE)

def merge(left_list, right_list, bars, buttons, win, execute_list, num_nums, nums, slider, MIN_RANGE, MAX_RANGE):
	sorted_list = []
	left_list_index = right_list_index = 0
	left_list_length, right_list_length = len(left_list), len(right_list)
	for _ in range(left_list_length + right_list_length):
		if left_list_index < left_list_length and right_list_index < right_list_length:
			if left_list[left_list_index] <= right_list[right_list_index]:
				sorted_list.append(left_list[left_list_index])
				left_list_index += 1
				redraw_bars(bars, win, num_nums, nums)
				draw_window(win, buttons, bars, execute_list, slider, num_nums, nums, MIN_RANGE, MAX_RANGE)
			else:
				sorted_list.append(right_list[right_list_index])
				right_list_index += 1
				redraw_bars(bars, win, num_nums, nums)
				draw_window(win, buttons, bars, execute_list, slider, num_nums, nums, MIN_RANGE, MAX_RANGE)
		elif left_list_index == left_list_length:
			sorted_list.append(right_list[right_list_index])
			right_list_index += 1
			redraw_bars(bars, win, num_nums, nums)
			draw_window(win, buttons, bars, execute_list, slider, num_nums, nums, MIN_RANGE, MAX_RANGE)
		elif right_list_index == right_list_length:
			sorted_list.append(left_list[left_list_index])
			left_list_index += 1
			redraw_bars(bars, win, num_nums, nums)
			draw_window(win, buttons, bars, execute_list, slider, num_nums, nums, MIN_RANGE, MAX_RANGE)
	return sorted_list

def merge_sort(nums, bars, buttons, win, execute_list, num_nums, slider, MIN_RANGE, MAX_RANGE):
	if len(nums) <= 1:
		return nums
	mid = len(nums) // 2
	left_list = merge_sort(nums[:mid], bars, buttons, win, execute_list, num_nums // 2, slider, MIN_RANGE, MAX_RANGE)
	right_list = merge_sort(nums[mid:], bars, buttons, win, execute_list, num_nums // 2, slider, MIN_RANGE, MAX_RANGE)
	return merge(left_list, right_list, bars, buttons, win, execute_list, num_nums, nums, slider, MIN_RANGE, MAX_RANGE)

def partition(nums, low, high, bars, buttons, win, execute_list, num_nums, slider, MIN_RANGE, MAX_RANGE):
	pivot = nums[(low + high) // 2]
	i = low - 1
	j = high + 1
	while True:
		i += 1
		while nums[i] < pivot:
			i += 1
		j -= 1
		while nums[j] > pivot:
			j -= 1
		if i >= j:
			return j
		nums[i], nums[j] = nums[j], nums[i]
		redraw_bars(bars, win, num_nums, nums)
		draw_window(win, buttons, bars, execute_list, slider, num_nums, nums, MIN_RANGE, MAX_RANGE)

def quick_sort(nums, bars, buttons, win, execute_list, num_nums, slider, MIN_RANGE, MAX_RANGE):
	def _quick_sort(items, low, high, bars, buttons, win, execute_list, num_nums, nums, slider, MIN_RANGE, MAX_RANGE):
		if low < high:
			split_index = partition(items, low, high, bars, buttons, win, execute_list, num_nums, slider, MIN_RANGE, MAX_RANGE)
			_quick_sort(items, low, split_index, bars, buttons, win, execute_list, num_nums, nums, slider, MIN_RANGE, MAX_RANGE)
			_quick_sort(items, split_index + 1, high, bars, buttons, win, execute_list, num_nums, nums, slider, MIN_RANGE, MAX_RANGE)
	_quick_sort(nums, 0, len(nums) - 1, bars, buttons, win, execute_list, num_nums, nums, slider, MIN_RANGE, MAX_RANGE)

def main():

	nums = []
	MIN_RANGE = 1
	MAX_RANGE = 1000
	num_nums = 100

	gen_array(nums, num_nums, MIN_RANGE, MAX_RANGE)

	clock = pygame.time.Clock()
	clock.tick(100)
	buttons = []
	bars = []
	bubble = False
	selection = False
	insertion = False
	heap = False
	merge = False
	quick = False
	new_array = False
	visualize = False
	execute_list = [bubble, selection, insertion, heap, merge, quick, new_array, visualize]

	for i in range(8):
			button = Button(i)
			buttons.append(button)

	for j in range(num_nums):
			bar = Bar(j, nums[j], num_nums, MIN_RANGE, MAX_RANGE)
			bars.append(bar)
	
	slider = Slider(num_nums)

	run = True
	while run:
		clock.tick(30)
		ev = pygame.event.get()
		for event in ev:
			if event.type == pygame.QUIT:
				run = False
				pygame.quit()
				quit()
			draw_window(win, buttons, bars, execute_list, slider, num_nums, nums, MIN_RANGE, MAX_RANGE)
			execute(execute_list, nums, bars, buttons, win, num_nums, slider, MIN_RANGE, MAX_RANGE)
			num_nums = change_num_nums(slider)
			if num_nums != len(nums):
				gen_array(nums, num_nums, MIN_RANGE, MAX_RANGE)
				redraw_bars(bars, win, num_nums, nums)

main()
